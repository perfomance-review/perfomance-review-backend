package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dao.ContentOfPollDao;
import ru.hh.performance_review.dao.PollDao;
import ru.hh.performance_review.dao.QuestionDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.dto.PollByUserIdResponseDto;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.request.CreatePollRequestDto;
import ru.hh.performance_review.dto.response.ComparePairsOfPollDto;
import ru.hh.performance_review.dto.response.EmptyResponseDto;
import ru.hh.performance_review.dto.response.PollByIdResponseDto;
import ru.hh.performance_review.dto.response.PollsByUserIdResponseDto;
import ru.hh.performance_review.dto.response.ResponseMessage;
import ru.hh.performance_review.dto.response.compairofpoll.ComparePairsOfPollInfoDto;
import ru.hh.performance_review.exception.ErrorCode;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;
import ru.hh.performance_review.mapper.ComparePairOfPollMapper;
import ru.hh.performance_review.mapper.ContentOfPollMapper;
import ru.hh.performance_review.mapper.PollMapper;
import ru.hh.performance_review.mapper.RespondentsOfPollMapper;
import ru.hh.performance_review.mapper.UserMapper;
import ru.hh.performance_review.model.ComparePair;
import ru.hh.performance_review.model.ContentOfPoll;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.model.Question;
import ru.hh.performance_review.model.RespondentsOfPoll;
import ru.hh.performance_review.model.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PollServiceImpl implements PollService {
    private final RespondentsOfPollDao respondentsOfPollDao;
    private final ContentOfPollDao contentOfPollDao;
    private final PollDao pollDao;
    private final ComparePairDao comparePairDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;
    private final PollMapper pollMapper;
    private final UserMapper userMapper;
    private final ComparePairOfPollMapper comparePairOfPollMapper;
    private final RespondentsOfPollMapper respondentsOfPollMapper;
    private final ContentOfPollMapper contentOfPollMapper;

    @Override
    public PollsByUserIdResponseDto getPollsByUserId(String userId, Set<String> statuses) {
        UUID userUUID = UUID.fromString(userId);
        Set<PollStatus> pollStatuses = statuses.stream()
            .map(PollStatus::valueOf)
            .collect(Collectors.toSet());
        Map<Poll, PollStatus> pollsStatus = respondentsOfPollDao
            .getByUserIdAndStatuses(userUUID, pollStatuses)
            .stream()
            .collect(Collectors.toMap(RespondentsOfPoll::getPoll, RespondentsOfPoll::getStatus));
        if (pollsStatus.size() == 0) {
            return new PollsByUserIdResponseDto(Collections.emptyList());
        }
        List<UUID> poolIds = pollsStatus.keySet()
            .stream()
            .map(Poll::getPollId)
            .collect(Collectors.toList());
        List<ContentOfPoll> content = contentOfPollDao.getByPollIds(poolIds);
        List<RespondentsOfPoll> respondents = respondentsOfPollDao.getByPollIds(poolIds);
        List<PollByUserIdResponseDto> polls = new ArrayList<>();

        for (Poll poll : pollsStatus.keySet()) {
            long respondentsCount = respondents
                .stream()
                .filter(o -> o.getPoll().equals(poll))
                .count();
            long questionsCount = content
                .stream()
                .filter(o -> o.getPoll().equals(poll))
                .count();
            polls.add(pollMapper.toPollByUserIdResponseDto(poll, respondentsCount, questionsCount, pollsStatus.get(poll)));
        }
        polls.sort(Comparator.comparing(PollByUserIdResponseDto::getDeadline).thenComparing(PollByUserIdResponseDto::getTitle));
        return new PollsByUserIdResponseDto(polls);
    }

    @Override
    public PollsByUserIdResponseDto getAllPollsByManagerId(String managerId) {

        List<Poll> allPollsForManager = pollDao.getAllByManagerId(UUID.fromString(managerId));
        if (CollectionUtils.isEmpty(allPollsForManager)) {
            return new PollsByUserIdResponseDto(Collections.emptyList());
        }
        List<UUID> pollIds = allPollsForManager.stream()
                .map(Poll::getPollId)
                .collect(Collectors.toList());
        List<RespondentsOfPoll> respondentsOfPollList = respondentsOfPollDao.getByPollIds(pollIds);
        List<ContentOfPoll> contentOfPollList = contentOfPollDao.getByPollIds(pollIds);

        List<PollByUserIdResponseDto> polls = new ArrayList<>();

        for (Poll poll : allPollsForManager) {
            long respondentsCount = respondentsOfPollList.stream()
                    .filter(x -> x.getPoll().equals(poll))
                    .count();
            long questionsCount = contentOfPollList.stream()
                    .filter(x -> x.getPoll().equals(poll))
                    .count();

            Set<PollStatus> statusesOfPoll = respondentsOfPollList.stream()
                    .filter(x->x.getPoll().equals(poll))
                    .map(RespondentsOfPoll::getStatus)
                    .collect(Collectors.toSet());

            // Если все записи respondentsOfPoll по опросу имеют одинаковый статус - то это статус опроса
            // Иначе - статус опроса PROGRESS (уже начат, но еще не завершен)
            PollStatus status = (statusesOfPoll.size() == 1) ? statusesOfPoll.iterator().next() : PollStatus.PROGRESS;

            polls.add(pollMapper.toPollByUserIdResponseDto(poll, respondentsCount, questionsCount, status));
        }
        polls.sort(Comparator.comparing(PollByUserIdResponseDto::getDeadline).thenComparing(PollByUserIdResponseDto::getTitle));
        return new PollsByUserIdResponseDto(polls);
    }


    @Override
    public PollByIdResponseDto getPollById(final String pollId, final String userId) {
        UUID uuid = UUID.fromString(pollId);
        Poll poll = pollDao.getByID(Poll.class, uuid);
        Map<Boolean, List<RespondentsOfPoll>> respondents = respondentsOfPollDao.getByPollId(uuid)
            .stream()
            .collect(Collectors
                .partitioningBy(o -> o.getRespondent().getUserId().equals(UUID.fromString(userId))));
        List<UserPollByIdResponseDto> respondentsDto = respondents.get(false)
            .stream()
            .map(o -> userMapper.toUserPollByIdResponseDto(o.getRespondent()))
            .collect(Collectors.toList());
        PollStatus status = respondents.get(true).get(0).getStatus();
        List<ContentOfPoll> content = contentOfPollDao.getByPollId(uuid);

        return pollMapper.toPollByIdResponseDto(poll, status, content.size(), respondentsDto);
    }

    /**
     * Пары берём из базы. Такие что winner id is Null (ещё не оценили).
     * В ответе должны быть: question_id, text (текст вопроса), все пары участники для вопроса, hasNext- есть ли след вопрос
     *
     * @param userIdStr - id респондента
     * @param pollIdStr - id опроса
     * @return - ComparePairsOfPollDto
     */
    @Override
    public ComparePairsOfPollDto getComparePairOfPollDto(String userIdStr, String pollIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        UUID pollId = UUID.fromString(pollIdStr);

        Optional<RespondentsOfPoll> respondentsOfPoll = respondentsOfPollDao.findOptionalByRespondentIdAndPollId(userId, pollId);

        if (respondentsOfPoll.isEmpty()) {
            throw new ValidateException(103, String.format("Не найден опрос по userId:%s и pollId:%s", userId, pollId));
        }

        respondentsOfPoll.ifPresent(respOfPoll -> {
            if (PollStatus.COMPLETED.equals(respOfPoll.getStatus())) {
                throw new ValidateException(105, String.format("Опрос завершен по userId:%s и pollId:%s", userId, pollId));
            }
        });
        log.info("respondentsOfPoll: {}", respondentsOfPoll.get().toString());
        List<ComparePair> comparePairs = comparePairDao.findAllUncompletedComparePairsByUserIdAndPollId(userId, pollId);
        log.info("comparePairs: {}", comparePairs.toString());
        Map<UUID, List<ComparePair>> stringListMap = comparePairs.stream()
                .collect(Collectors.groupingBy(comparePair -> comparePair.getQuestion().getQuestionId()));
        log.info("stringListMap: {}", stringListMap.toString());
        Set<UUID> questionIds = stringListMap.keySet();
        log.info("questionIds: {}", questionIds.toString());
        List<ContentOfPoll> contentOfPolls = CollectionUtils.isEmpty(questionIds)
                ? Collections.emptyList()
                : contentOfPollDao.findByPollIdAndQuestionIds(pollId, questionIds);
        log.info("contentOfPolls: {}", contentOfPolls.toString());
        ContentOfPoll contentOfPollMin = getContentOfPollMin(contentOfPolls, questionIds, pollId);
        ContentOfPoll contentOfPollMax = getContentOfPollMax(contentOfPolls, questionIds, pollId);
        //пока максимальный != минимальным по ордеру, значит есть еще вопросы
        boolean hasNext = !contentOfPollMin.getId().equals(contentOfPollMax.getId());

        Question question = contentOfPollMin.getQuestion();
        UUID questionId = question.getQuestionId();

        List<ComparePair> comparePairList = stringListMap.get(questionId);

        List<ComparePairsOfPollInfoDto> pairsOfPollInfo = comparePairOfPollMapper.toComparePairsOfPollInfoDtos(comparePairList);
        Collections.shuffle(pairsOfPollInfo);

        return new ComparePairsOfPollDto()
                .setPollId(pollIdStr)
                .setRespondentId(userIdStr)
                .setQuestionId(questionId)
                .setText(question.getText())
                .setHasNext(hasNext)
                .setPairsOfPollInfo(pairsOfPollInfo);
    }

    @Override
    public ResponseMessage createPoll(final CreatePollRequestDto request, String managerId) {
        User manager = userDao.getByID(User.class, UUID.fromString(managerId));

        Poll poll = pollMapper.fromCreatePollRequestDto(request, manager);
        pollDao.save(poll);

        List<UUID> usersIds = request.getRespondentIds().stream()
            .map(UUID::fromString)
            .collect(Collectors.toList());
        Map<UUID, User> userMap = userDao.getAllByIds(usersIds).stream()
            .collect(Collectors.toMap(User::getUserId, Function.identity()));

        for (UUID userId : usersIds) {
            User respondent = userMap.get(userId);
            if (respondent == null) {
               throw new ValidateException(2, String.format("Не найден пользователь: %s", userId));
            }
            User leader = respondent.getLeader();
            if (leader == null || !leader.getUserId().toString().equals(managerId)) {
                throw new ValidateException(5, String.format("Пользователь %s не соответствует менеджеру: %s", userId, managerId));

            }
            RespondentsOfPoll respondentOfPoll = respondentsOfPollMapper.toRespondentsOfPoll(poll, respondent, PollStatus.OPEN);
            respondentsOfPollDao.save(respondentOfPoll);
        }

        List<UUID> questionIds = request.getQuestionIds().stream()
            .map(UUID::fromString)
            .collect(Collectors.toList());
        Map<UUID, Question> questionMap = questionDao.getAllByIds(questionIds).stream()
            .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));

        for (int i = 0; i < questionIds.size(); i++) {
            UUID questionId = questionIds.get(i);
            Question question = questionMap.get(questionId);
            if (question == null) {
                throw new ValidateException(4, String.format("Не найден вопрос по questionId %s", questionId));
            }
            ContentOfPoll contentOfPoll = contentOfPollMapper.toContentOfPoll(poll, question, i + 1);
            contentOfPollDao.save(contentOfPoll);
        }
        return new EmptyResponseDto();
    }

    private ContentOfPoll getContentOfPollMin(List<ContentOfPoll> contentOfPolls,
                                              Set<UUID> questionIds, UUID pollId) {
        Optional<ContentOfPoll> optionalContentOfPollMax = contentOfPolls
                .stream()
                .min(Comparator.comparing(ContentOfPoll::getOrder));

        if (optionalContentOfPollMax.isEmpty()) {
            throw new ValidateException(103, String.format("Не найдена запись content_of_poll по questionIds:%s и pollId:%s", questionIds, pollId));
        }

        return optionalContentOfPollMax.get();
    }

    private ContentOfPoll getContentOfPollMax(List<ContentOfPoll> contentOfPolls,
                                              Set<UUID> questionIds, UUID pollId) {
        Optional<ContentOfPoll> optionalContentOfPollMax = contentOfPolls
                .stream()
                .max(Comparator.comparing(ContentOfPoll::getOrder));

        if (optionalContentOfPollMax.isEmpty()) {
            throw new ValidateException(103, String.format("Не найдена запись content_of_poll по questionIds:%s и pollId:%s", questionIds, pollId));
        }

        return optionalContentOfPollMax.get();
    }
}
