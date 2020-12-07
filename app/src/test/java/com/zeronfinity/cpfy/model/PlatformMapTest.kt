package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Platform
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class PlatformMapTest {
    private val platform1 = Platform("name1", "imageUrl1")
    private val platform2 = Platform("name2", "imageUrl2")
    private val platform1Dup = Platform("name1", "imageUrl3")
    private val platform2Dup = Platform("name2", "imageUrl4")
    private val platform3 = Platform("name3", "imageUrl3")
    private val platform4 = Platform("name4", "imageUrl4")
    private val platformListPreInserted = arrayListOf(platform3, platform4)


    private val sut = PlatformMap()

    @Nested
    @DisplayName("Given no platforms added before")
    inner class EmptyPlatformMap {
        @Test
        @DisplayName("When platforms added one at a time, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name added one at a time, then image of last one is fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1Dup.name)) }
            )
        }

        @Test
        @DisplayName("When multiple duplicate platforms added one at a time, then images are fetched correctly")
        fun addAndGetImageUrl_multipleDuplicatePlatformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1Dup.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2Dup.name)) }
            )
        }

        @Test
        @DisplayName("When platforms added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name are added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2Dup.name)) }
            )
        }
    }

    @Nested
    @DisplayName("Given some platforms added before")
    inner class NonEmptyPlatformMap {
        init {
            sut.add(platformListPreInserted)
        }

        @Test
        @DisplayName("When platforms added one at a time, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name added one at a time, then image of last one is fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1Dup.name)) }
            )
        }

        @Test
        @DisplayName("When multiple duplicate platforms added one at a time, then images are fetched correctly")
        fun addAndGetImageUrl_multipleDuplicatePlatformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform1Dup.imageUrl, sut.getImageUrl(platform1Dup.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2Dup.name)) }
            )
        }

        @Test
        @DisplayName("When platforms added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name are added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1.imageUrl, sut.getImageUrl(platform1.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2.name)) },
                Executable { assertEquals(platform2.imageUrl, sut.getImageUrl(platform2Dup.name)) }
            )
        }

        @Test
        @DisplayName("When getImageUrl called with existing name, then correct image url is fetched correctly")
        fun getImageUrl_existingNameUsed_correctUrlReturned() {
            // Arrange
            // Act
            val imageUrl3 = sut.getImageUrl(platform3.name)
            val imageUrl4 = sut.getImageUrl(platform4.name)
            // Assert
            assertAll(
                Executable { assertEquals(platform3.imageUrl, imageUrl3) },
                Executable { assertEquals(platform4.imageUrl, imageUrl4) }
            )
        }

        @Test
        @DisplayName("When getImageUrl called with non-existing name, then null is returned")
        fun getImageUrl_nonExistingNameUsed_emptyUrlReturned() {
            // Arrange
            // Act
            val imageUrl1 = sut.getImageUrl(platform1.name)
            // Assert
            assertEquals(null, imageUrl1)
        }

        @Test
        @DisplayName("When all platforms are removed, then no data exists")
        fun removeAll_noDataExists() {
            // Arrange
            // Act
            sut.removeAll()
            // Assert
            assertAll(
                Executable { assertEquals(null, sut.getImageUrl(platform3.name)) },
                Executable { assertEquals(null, sut.getImageUrl(platform4.name)) }
            )
        }
    }
}