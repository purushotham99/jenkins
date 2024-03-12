package hudson.search;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionSearchIndexTest {
    private CollectionSearchIndex<SearchableModelObject> index;
    SearchItem mockItem = mock(SearchItem.class);
    SearchableModelObject mockItem2 = mock(SearchableModelObject.class);
    SearchableModelObject mockItem3 = mock(SearchableModelObject.class);

    @BeforeEach
    void setUp() {
        index = new CollectionSearchIndex<SearchableModelObject>() {
            @Override
            protected SearchItem get(String key) {
                return mockItem;
            }

            @Override
            protected Collection<SearchableModelObject> all() {
                Collection<SearchableModelObject> mockCollection = new ArrayList<>();

                mockCollection.add(mockItem3);
                mockCollection.add(mockItem2);

                return mockCollection;
            }

            @Override
            protected String getName(SearchableModelObject o) {
                return "test";
            }

            @Override
            protected Iterable<SearchableModelObject> allAsIterable() {
                Collection<SearchableModelObject> all = all();
                return all == null ? Collections.emptySet() : all;
            }
        };
    }

    @Test
    void testFindExactMatch() {


        List<SearchItem> result = new ArrayList<>();
        index.find("token", result);

        assertEquals(1, result.size());
        assertEquals(mockItem, result.get(0));
    }

    @Test
    void testFindNoMatch() {
        List<SearchItem> result = new ArrayList<>();
        index.find("nonexistent", result);

        assertEquals(1, result.size());
    }

    @Test
    void testSuggestTokenFound() {

        // Test
        List<SearchItem> result = new ArrayList<>();
        index.suggest("test", result);

        assertEquals(2, result.size());
        assertEquals(mockItem3, result.get(0));
    }
}
