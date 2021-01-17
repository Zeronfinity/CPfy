package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Contest
import com.zeronfinity.cpfy.framework.db.dao.ContestDao
import com.zeronfinity.cpfy.framework.db.entity.ContestEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date

internal class ContestDbTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private val contest1 = Contest(1, "title1", 1, 1, Date(1), Date(2),"url1")
    private val contest2 = Contest(2, "title2", 2, 2, Date(2), Date(4),"url2")
    private val contest3 = Contest(3, "title3", 3, 3, Date(3), Date(6),"url3")
    private val contest4 = Contest(4, "title4", 4, 4, Date(4), Date(8),"url4")
    private val contestListAdded = arrayListOf(contest1, contest2, contest1)
    private val contestListExpected = arrayListOf(contest1, contest2)
    private val contestListPreInserted = arrayListOf(contest3, contest4)

    private val sut = ContestDb(ContestDaoTd())

    @Nested
    @DisplayName("Given empty contest list")
    inner class EmptyContestList {
        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When new contest list added, then contests are added correctly")
        fun add_correctContestsInserted() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.add(contestListAdded)
                // Assert
                val contestListActual = sut.getList()
                assertEquals(contestListExpected, contestListActual)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getList called, empty list returned")
        fun getList_emptyListReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val list = sut.getList()
                // Assert
                assertEquals(0, list?.size)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When new contest list added, then size is returned correctly")
        fun size_correctSizeReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.add(contestListAdded)
                // Assert
                assertEquals(contestListExpected.size, sut.size())
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Nested
    @DisplayName("Given non-empty contest list")
    inner class NonEmptyContestList {
        init {
            testDispatcher.runBlockingTest {
                sut.add(contestListPreInserted)
            }
        }

        @Test
        @DisplayName("When new contest list added, then contests are added correctly")
        fun add_correctContestsInserted() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val list = contestListExpected.toList() as ArrayList
                sut.add(contestListAdded)
                // Assert
                val contestListActual = sut.getList()
                list.addAll(contestListPreInserted)
                assertEquals(list, contestListActual)
            }
        }

        @Test
        @DisplayName("When get is called with proper id, then correct contest is returned correctly")
        fun get_properIdUsed_correctContestReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val contestActual = sut.get(3)
                // Assert
                assertEquals(contest3, contestActual)
            }
        }

        @Test
        @DisplayName("When get is called with out of bound id, then null is returned correctly")
        fun get_outOfBoundIndexUsed_correctExceptionThrown() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act and Assert
                assertEquals(null, sut.get(100))
            }
        }

        @Test
        @DisplayName("When getList called, pre-inserted list returned")
        fun getList_correctListReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val list = sut.getList()
                // Assert
                assertEquals(contestListPreInserted, list)
            }
        }

        @Test
        @DisplayName("When new contest list added, then size is returned correctly")
        fun size_correctSizeReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.add(contestListAdded)
                // Assert
                assertEquals(contestListExpected.size + contestListPreInserted.size, sut.size())
            }
        }

        @Test
        @DisplayName("When contest list cleared, then size is returned empty")
        fun clear_contestListClearedSuccessfully() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.clear()
                // Assert
                assertEquals(0, sut.size())
            }
        }
    }

    class ContestDaoTd : ContestDao {
        private val contestMap = sortedMapOf<Int, ContestEntity>()

        override suspend fun deleteAll() {
            contestMap.clear()
        }

        override suspend fun getContest(id: Int): ContestEntity? {
            return contestMap[id]
        }

        override suspend fun getContestAll(): List<ContestEntity>? {
            return contestMap.values.toList()
        }

        override fun getContestAllFlow(): Flow<List<ContestEntity>> {
            return flow {
                emit(contestMap.values.toList())
            }
        }

        override suspend fun getRowCount(): Int? {
            return contestMap.size
        }

        override suspend fun insert(vararg obj: ContestEntity) {
            for (contest in obj) {
                contestMap[contest.id] = contest
            }
        }
    }
}
