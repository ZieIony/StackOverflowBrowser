package com.github.zieiony.stackoverflowbrowser.api;

import com.github.zieiony.stackoverflowbrowser.api.data.Question;
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingOrder;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingType;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowAPI;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import io.reactivex.Observable;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserDataRepositoryTest {

    private QuestionRepository questionRepository;

    @Mock
    private StackOverflowAPI stackOverflowAPI;

    @Before
    public void setUp() {
        questionRepository = new QuestionRepositoryImpl(new StackOverflowService(stackOverflowAPI));
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

        Observable<QuestionsResponse> responseObservable = questionRepository.getQuestions("java", 5);
        QuestionsResponse response1 = responseObservable.blockingFirst();
        assert Arrays.equals(response1.getItems(), questions);

        verify(stackOverflowAPI).searchQuestions(anyString(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString());
    }
}
