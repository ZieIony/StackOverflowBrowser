package com.github.zieiony.stackoverflowbrowser.api;

import com.github.zieiony.stackoverflowbrowser.api.data.Question;
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import io.reactivex.Observable;
import tk.zielony.dataapi.Response;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserDataRepositoryTest {

    private IQuestionRepository questionRepository;

    @Mock
    private StackOverflowAPI stackOverflowAPI;

    @Before
    public void setUp() {
        questionRepository = new QuestionRepository(stackOverflowAPI);
    }

    @Test
    public void testGetQuestions() {
        Question[] questions = {
                new Question(),
                new Question()
        };
        QuestionsResponse questionsResponse = new QuestionsResponse();
        questionsResponse.setItems(questions);
        Response<QuestionsResponse> response = new Response<>(HttpMethod.GET, "endpoint", questionsResponse, HttpStatus.OK);
        given(stackOverflowAPI.searchQuestions(anyString(), anyInt(), any())).willReturn(Observable.just(response));

        Observable<Response<QuestionsResponse>> responseObservable = questionRepository.getQuestions("java", 5);
        Response<QuestionsResponse> response1 = responseObservable.blockingFirst();
        assert Arrays.equals(response1.getData().getItems(), questions);

        verify(stackOverflowAPI).searchQuestions(any(), anyInt(), any());
    }
}
