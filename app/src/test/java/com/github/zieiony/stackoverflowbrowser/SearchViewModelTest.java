package com.github.zieiony.stackoverflowbrowser;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.SavedStateHandle;

import com.github.zieiony.stackoverflowbrowser.api.QuestionRepository;
import com.github.zieiony.stackoverflowbrowser.api.QuestionRepositoryImpl;
import com.github.zieiony.stackoverflowbrowser.api.data.Question;
import com.github.zieiony.stackoverflowbrowser.api.data.QuestionsResponse;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingOrder;
import com.github.zieiony.stackoverflowbrowser.api.web.SortingType;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowAPI;
import com.github.zieiony.stackoverflowbrowser.api.web.StackOverflowService;
import com.github.zieiony.stackoverflowbrowser.search.GetQuestionsInteractor;
import com.github.zieiony.stackoverflowbrowser.search.SearchState;
import com.github.zieiony.stackoverflowbrowser.search.SearchViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;


@RunWith(MockitoJUnitRunner.class)
public class SearchViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private SearchViewModel searchViewModel;

    @Mock
    private StackOverflowAPI stackOverflowAPI;

    @Before
    public void setUp() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        QuestionRepository questionRepository = new QuestionRepositoryImpl(new StackOverflowService(stackOverflowAPI));
        GetQuestionsInteractor getQuestionsInteractor = new GetQuestionsInteractor(questionRepository);
        searchViewModel = new SearchViewModel(new SavedStateHandle(), getQuestionsInteractor);
    }

    @Test
    public void testLoadQuestionsPage() {
        Question[] questions = {
                new Question(),
                new Question()
        };
        QuestionsResponse questionsResponse = new QuestionsResponse();
        questionsResponse.setItems(questions);
        questionsResponse.setHas_more(true);
        given(stackOverflowAPI.searchQuestions(anyString(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString())).willReturn(Observable.just(questionsResponse));

        searchViewModel.loadFirstPage("Java");

        assert searchViewModel.getState().getValue() instanceof SearchState.Results;
        SearchState.Results results = (SearchState.Results) searchViewModel.getState().getValue();
        assert Arrays.equals(results.getItems(), questions);

        searchViewModel.loadNextPage();

        assert searchViewModel.getState().getValue() instanceof SearchState.Results;
        SearchState.Results results2 = (SearchState.Results) searchViewModel.getState().getValue();
        assert Arrays.equals(results2.getItems(), new Question[]{questions[0], questions[1], questions[0], questions[1]});
    }

    @Test
    public void testLoadQuestionsPage_invalidResponse() {
        QuestionsResponse questionsResponse = new QuestionsResponse();
        given(stackOverflowAPI.searchQuestions(anyString(), anyInt(), anyInt(), any(SortingOrder.class), any(SortingType.class), anyString(), anyString())).willReturn(Observable.just(questionsResponse));

        searchViewModel.loadFirstPage("Java");

        assert searchViewModel.getState().getValue() instanceof SearchState.Error;
    }

}
