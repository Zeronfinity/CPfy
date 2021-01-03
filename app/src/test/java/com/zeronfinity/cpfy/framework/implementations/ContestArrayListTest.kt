package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Contest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date

internal class ContestArrayListTest {
    private val contest1 = Contest("title1", 1, 1, Date(1), Date(2),"url1")
    private val contest2 = Contest("title2", 2, 2, Date(2), Date(4),"url2")
    private val contest3 = Contest("title3", 3, 3, Date(3), Date(6),"url3")
    private val contest4 = Contest("title4", 4, 4, Date(4), Date(8),"url4")
    private val contestListExpected = arrayListOf(contest1, contest2, contest1)
    private val contestListPreInserted = arrayListOf(contest3, contest4)

    private val sut = ContestArrayList()

    @Nested
    @DisplayName("Given empty contest list")
    inner class EmptyContestList {
        @Test
        @DisplayName("When new contest list added, then contests are added correctly")
        fun add_correctContestsInserted() {
            // Arrange
            // Act
            sut.add(contestListExpected)
            // Assert
            val contestListActual = ArrayList<Contest>()
            for (i in 0 until sut.size()) {
                contestListActual.add(sut.get(i))
            }
            assertEquals(contestListExpected, contestListActual)
        }

        @Test
        @DisplayName("When getList called, empty list returned")
        fun getList_emptyListReturned() {
            // Arrange
            // Act
            val list = sut.getList()
            // Assert
            assertEquals(0, list.size)
        }

        @Test
        @DisplayName("When new contest list added, then size is returned correctly")
        fun size_correctSizeReturned() {
            // Arrange
            // Act
            sut.add(contestListExpected)
            // Assert
            assertEquals(contestListExpected.size, sut.size())
        }
    }

    @Nested
    @DisplayName("Given non-empty contest list")
    inner class NonEmptyContestList {
        init {
            sut.add(contestListPreInserted)
        }

        @Test
        @DisplayName("When new contest list added, then contests are added correctly")
        fun add_correctContestsInserted() {
            // Arrange
            // Act
            sut.add(contestListExpected)
            // Assert
            val contestListActual = ArrayList<Contest>()
            for (i in 0 until contestListExpected.size) {
                contestListActual.add(sut.get(sut.size() - contestListExpected.size + i))
            }
            assertEquals(contestListExpected, contestListActual)
        }

        @Test
        @DisplayName("When get is called with proper index, then correct contest is returned correctly")
        fun get_properIndexUsed_correctContestReturned() {
            // Arrange
            // Act
            val contestActual = sut.get(1)
            // Assert
            assertEquals(contest4, contestActual)
        }

        @Test
        @DisplayName("When get is called with out of bound index, then correct contest is returned correctly")
        fun get_outOfBoundIndexUsed_correctExceptionThrown() {
            // Arrange
            // Act and Assert
            assertThrows(IndexOutOfBoundsException::class.java) { sut.get(contestListPreInserted.size + 1) }
        }

        @Test
        @DisplayName("When getList called, empty list returned")
        fun getList_correctListReturned() {
            // Arrange
            // Act
            val list = sut.getList()
            // Assert
            assertEquals(contestListPreInserted, list)
        }

        @Test
        @DisplayName("When new contest list added, then size is returned correctly")
        fun size_correctSizeReturned() {
            // Arrange
            // Act
            sut.add(contestListExpected)
            // Assert
            assertEquals(contestListExpected.size + contestListPreInserted.size, sut.size())
        }

        @Test
        @DisplayName("When contest list cleared, then size is returned empty")
        fun clear_contestListClearedSuccessfuly() {
            // Arrange
            // Act
            sut.clear()
            // Assert
            assertEquals(0, sut.size())
        }
    }
}
