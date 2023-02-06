package au.com.shiftyjelly.pocketcasts.models.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import au.com.shiftyjelly.pocketcasts.models.db.dao.SearchHistoryDao
import au.com.shiftyjelly.pocketcasts.models.entity.SearchHistoryItem
import au.com.shiftyjelly.pocketcasts.models.entity.SearchHistoryItem.Folder
import au.com.shiftyjelly.pocketcasts.models.entity.SearchHistoryItem.Podcast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import java.util.UUID

private const val SEARCH_TERM_TEST1 = "test1"
private const val SEARCH_TERM_TEST2 = "test2"

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SearchHistoryDaoTest {
    lateinit var searchHistoryDao: SearchHistoryDao
    lateinit var testDb: AppDatabase

    @Before
    fun setupDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        searchHistoryDao = testDb.searchHistoryDao()
    }

    @After
    fun closeDb() {
        testDb.close()
    }

    /* INSERT */
    @Test
    fun testInsertSearchTerm() = runTest {
        searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST1))

        assertTrue(searchHistoryDao.findAll().first().term == SEARCH_TERM_TEST1)
    }

    @Test
    fun testInsertPodcastSearchHistory() {
        val uuid = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createPodcastSearchHistoryItem(uuid))

            assertTrue(searchHistoryDao.findAll().first().podcast?.uuid == uuid)
        }
    }

    @Test
    fun testInsertFolderSearchHistory() {
        val uuid = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createFolderSearchHistoryItem(uuid))

            assertTrue(searchHistoryDao.findAll().first().folder?.uuid == uuid)
        }
    }

    @Test
    fun testInsertEpisodeSearchHistory() {
        val uuid = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createEpisodeSearchHistoryItem(uuid))

            assertTrue(searchHistoryDao.findAll().first().episode?.uuid == uuid)
        }
    }

    /* MULTIPLE INSERT OR REPLACE */
    @Test
    fun testMultipleInsertSameSearchTerms() {
        runTest {
            searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST1))
            val modifiedPrevious = searchHistoryDao.findAll().first().modified
            searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST1))

            val result = searchHistoryDao.findAll()
            assertEquals("Insert should replace, count should be 1", 1, result.size)
            assertEquals(
                "Replaced search term should be on top",
                true,
                result.first().modified > modifiedPrevious
            )
        }
    }

    @Test
    fun testMultipleInsertUniqueSearchTerms() {
        runTest {
            searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST1))
            searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST2))

            val result = searchHistoryDao.findAll()
            assertEquals("Unique search terms should be inserted, count should be 2", 2, result.size)
            assertEquals(
                "Last search term inserted should be on top",
                SEARCH_TERM_TEST2,
                result.first().term
            )
        }
    }

    @Test
    fun testMultipleInsertSamePodcastSearchHistory() {
        val uuid = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createPodcastSearchHistoryItem(uuid = uuid))
            val modifiedPrevious = searchHistoryDao.findAll().first().modified
            searchHistoryDao.insert(createPodcastSearchHistoryItem(uuid = uuid))

            val result = searchHistoryDao.findAll()
            assertEquals("Same podcast search insert should replace, count should be 1", 1, result.size)
            assertEquals(
                "Replaced podcast search history item should be on top",
                true,
                result.first().modified > modifiedPrevious
            )
        }
    }

    @Test
    fun testMultipleInsertUniquePodcastSearchHistory() {
        val uuid1 = UUID.randomUUID().toString()
        val uuid2 = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createPodcastSearchHistoryItem(uuid1))
            searchHistoryDao.insert(createPodcastSearchHistoryItem(uuid2))

            val result = searchHistoryDao.findAll()
            assertEquals("Unique podcast search history should be inserted, count should be 2", 2, result.size)
            assertEquals(
                "Last podcast search history inserted should be on top",
                uuid2,
                result.first().podcast?.uuid
            )
        }
    }

    @Test
    fun testMultipleInsertSameFolderSearchHistory() {
        val uuid = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createFolderSearchHistoryItem(uuid))
            val modifiedPrevious = searchHistoryDao.findAll().first().modified
            searchHistoryDao.insert(createFolderSearchHistoryItem(uuid))

            val result = searchHistoryDao.findAll()
            assertEquals("Same folder search insert should replace, count should be 1", 1, result.size)
            assertEquals(
                "Replaced folder search should be on top",
                true,
                result.first().modified > modifiedPrevious
            )
        }
    }

    @Test
    fun testMultipleInsertUniqueFolderSearchHistory() {
        val uuid1 = UUID.randomUUID().toString()
        val uuid2 = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createFolderSearchHistoryItem(uuid1))
            searchHistoryDao.insert(createFolderSearchHistoryItem(uuid2))

            val result = searchHistoryDao.findAll()
            assertEquals("Unique folder search history should be inserted, count should be 2", 2, result.size)
            assertEquals(
                "Last folder search history inserted should be on top",
                uuid2,
                result.first().folder?.uuid
            )
        }
    }

    @Test
    fun testMultipleInsertSameEpisodeSearchHistory() {
        val uuid = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createEpisodeSearchHistoryItem(uuid))
            val modifiedPrevious = searchHistoryDao.findAll().first().modified
            searchHistoryDao.insert(createEpisodeSearchHistoryItem(uuid))

            val result = searchHistoryDao.findAll()
            assertEquals("Same episode insert should replace, count should be 1", 1, result.size)
            assertEquals(
                "Replaced episode search should be on top",
                true,
                result.first().modified > modifiedPrevious
            )
        }
    }

    @Test
    fun testMultipleInsertUniqueEpisodeSearchHistory() {
        val uuid1 = UUID.randomUUID().toString()
        val uuid2 = UUID.randomUUID().toString()
        runTest {
            searchHistoryDao.insert(createEpisodeSearchHistoryItem(uuid1))
            searchHistoryDao.insert(createEpisodeSearchHistoryItem(uuid2))

            val result = searchHistoryDao.findAll()
            assertEquals("Unique episode search history should be inserted, count should be 2", 2, result.size)
            assertEquals(
                "Last episode search history inserted should be on top",
                uuid2,
                result.first().episode?.uuid
            )
        }
    }

    /* DELETE */
    @Test
    fun testDeleteSearchHistoryItem() {
        runTest {
            searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST1))

            searchHistoryDao.delete(searchHistoryDao.findAll().first())

            assertTrue(searchHistoryDao.findAll().isEmpty())
        }
    }

    @Test
    fun testDeleteAllSearchHistory() {
        runTest {
            val uuid = UUID.randomUUID().toString()
            searchHistoryDao.insert(createTermSearchHistoryItem(SEARCH_TERM_TEST1))
            searchHistoryDao.insert(createPodcastSearchHistoryItem(uuid))
            searchHistoryDao.insert(createFolderSearchHistoryItem(uuid))
            searchHistoryDao.insert(createEpisodeSearchHistoryItem(uuid))

            searchHistoryDao.deleteAll()

            assertTrue(searchHistoryDao.findAll().isEmpty())
        }
    }

    /* HELPER FUNCTIONS */
    private fun createTermSearchHistoryItem(term: String) =
        SearchHistoryItem(term = term)

    private fun createPodcastSearchHistoryItem(uuid: String) =
        SearchHistoryItem(
            podcast = Podcast(
                uuid = uuid,
                title = "",
                author = "",
            )
        )

    private fun createFolderSearchHistoryItem(uuid: String) =
        SearchHistoryItem(folder = Folder(uuid = uuid, title = "", color = 0, podcastIds = ""))

    private fun createEpisodeSearchHistoryItem(uuid: String) =
        SearchHistoryItem(
            episode = SearchHistoryItem.Episode(
                uuid = uuid,
                title = "",
                publishedDate = Date(),
                duration = 0.0,
            )
        )
}
