package com.github.zieiony.stackoverflowbrowser;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.SavedStateHandle;

import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository;
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepositoryImpl;
import com.github.zieiony.stackoverflowbrowser.api.data.Answer;
import com.github.zieiony.stackoverflowbrowser.api.data.AnswersResponse;
import com.github.zieiony.stackoverflowbrowser.api.data.Question;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingOrder;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingType;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowAPI;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService;
import com.github.zieiony.stackoverflowbrowser.question.GetAnswersInteractor;
import com.github.zieiony.stackoverflowbrowser.question.QuestionState;
import com.github.zieiony.stackoverflowbrowser.question.QuestionViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;


@RunWith(MockitoJUnitRunner.class)
public class QuestionViewModelTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private QuestionViewModel questionsViewModel;

    @Mock
    private StackOverflowAPI stackOverflowAPI;

    @Before
    public void setUp() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        QuestionRepository questionRepository = new QuestionRepositoryImpl(new StackOverflowService(stackOverflowAPI));
        GetAnswersInteractor getAnswersInteractor = new GetAnswersInteractor(questionRepository);
        questionsViewModel = new QuestionViewModel(new SavedStateHandle(), getAnswersInteractor);
    }

    @Test
    public void testLoadQuestionsPage() {
        Answer[] answers = {
                new Answer(),
                new Answer()
        };
        AnswersResponse answersResponse = new AnswersResponse();
        answersResponse.setItems(answers);
        answersResponse.setHas_more(true);
        given(stackOverflowAPI.requestAnswers(anyLong(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString())).willReturn(Observable.just(answersResponse));

        Question question = new Question();
        question.setQuestion_id(0L);
        questionsViewModel.loadFirstPage(question);

        assert questionsViewModel.getState().getValue() instanceof QuestionState.Results;
        QuestionState.Results results = (QuestionState.Results) questionsViewModel.getState().getValue();
        assert Arrays.equals(results.getItems(), new Serializable[]{question, answers[0], answers[1]});

        questionsViewModel.loadNextPage();

        assert questionsViewModel.getState().getValue() instanceof QuestionState.Results;
        QuestionState.Results results2 = (QuestionState.Results) questionsViewModel.getState().getValue();
        assert Arrays.equals(results2.getItems(), new Serializable[]{question, answers[0], answers[1], answers[0], answers[1]});
    }

    @Test
    public void testLoadQuestionsPage_invalidResponse() {
        AnswersResponse answersResponse = new AnswersResponse();
        given(stackOverflowAPI.requestAnswers(anyLong(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString())).willReturn(Observable.just(answersResponse));

        Question question = new Question();
        question.setQuestion_id(0L);
        questionsViewModel.loadFirstPage(question);

        assert questionsViewModel.getState().getValue() instanceof QuestionState.Error;
    }

}
