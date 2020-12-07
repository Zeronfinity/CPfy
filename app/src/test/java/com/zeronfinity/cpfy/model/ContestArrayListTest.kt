package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Contest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date

internal class ContestArrayListTest {
    private val contest1 = Contest("title1", 1, "platform1", Date(1), "url1")
    private val contest2 = Contest("title2", 2, "platform2", Date(2), "url2")
    private val contest3 = Contest("title3", 3, "platform3", Date(3), "url3")
    private val contest4 = Contest("title4", 4, "platform4", Date(4), "url4")
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