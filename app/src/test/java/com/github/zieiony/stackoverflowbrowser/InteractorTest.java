package com.github.zieiony.stackoverflowbrowser;

import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository;
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepositoryImpl;
import com.github.zieiony.stackoverflowbrowser.api.data.Answer;
import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse;
import com.github.zieiony.stackoverflowbrowser.api.data.Question;
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingOrder;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingType;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowAPI;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService;
import com.github.zieiony.stackoverflowbrowser.question.GetAnswersInteractor;
import com.github.zieiony.stackoverflowbrowser.search.GetQuestionsInteractor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;


@RunWith(MockitoJUnitRunner.class)
public class InteractorTest {

    private GetQuestionsInteractor getQuestionsInteractor;
    private GetAnswersInteractor getAnswersInteractor;

    @Mock
    private StackOverflowAPI stackOverflowAPI;

    @Before
    public void setUp() {
        QuestionRepository questionRepository = new QuestionRepositoryImpl(new StackOverflowService(stackOverflowAPI));
        getQuestionsInteractor = new GetQuestionsInteractor(questionRepository);
        getAnswersInteractor = new GetAnswersInteractor(questionRepository);
    }

    @Test
    public void testGetQuestions() {
        Question[] questions = {
                new Question(),
                new Question()
        };
        QuestionsResponse questionsResponse = new QuestionsResponse();
        questionsResponse.setItems(questions);
        given(stackOverflowAPI.searchQuestions(anyString(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString())).willReturn(Observable.just(questionsResponse));

        QuestionsResponse response = getQuestionsInteractor.execute("Java", 5).blockingFirst();

        assert Arrays.equals(response.getItems(), questions);
    }

    @Test
    public void testGetAnswers() {
        Answer[] answers = {
                new Answer(),
                new Answer()
        };
        AnswersResponse answersResponse = new AnswersResponse();
        answersResponse.setItems(answers);
        given(stackOverflowAPI.requestAnswers(anyLong(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString())).willReturn(Observable.just(answersResponse));

        Question question = new Question();
        question.setQuestion_id(0L);
        AnswersResponse response = getAnswersInteractor.execute(question, 5).blockingFirst();

        assert Arrays.equals(response.getItems(), answers);
    }
}
