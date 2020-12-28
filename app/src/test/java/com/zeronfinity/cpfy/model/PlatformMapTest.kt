package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Platform
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class PlatformMapTest {
    private val platform1 = Platform("name1", "imageUrl1", "shortName1")
    private val platform2 = Platform("name2", "imageUrl2", "shortName2")
    private val platform1Dup = Platform("name1", "imageUrl3", "shortName1")
    private val platform2Dup = Platform("name2", "imageUrl4", "shortName2")
    private val platform3 = Platform("name3", "imageUrl3", "shortName3")
    private val platform4 = Platform("name4", "imageUrl4", "shortName4")
    private val platformListPreInserted = arrayListOf(platform4, platform3)

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

        @Test
        @DisplayName("When platforms added one at a time, then getPlatform works correctly")
        fun addAndGetPlatform_platformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name added one at a time, then last platform of a name is fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1Dup.name)) }
            )
        }

        @Test
        @DisplayName("When multiple duplicate platforms added one at a time, then platforms are fetched correctly")
        fun addAndGetPlatform_multipleDuplicatePlatformsInsertedOneByOne_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1Dup.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2Dup.name)) }
            )
        }

        @Test
        @DisplayName("When platforms added together as a list, then platforms are fetched correctly")
        fun addAndGetPlatform_platformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name are added together as a list, then they are fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2Dup.name)) }
            )
        }

        @Test
        @DisplayName("When no platform added, size returns zero")
        fun getPlatformList_noPlatformInserted_emptyListReturned() {
            // Arrange
            // Act
            val list = sut.getPlatformList()
            // Assert
            assertEquals(0, list.size)
        }

        @Test
        @DisplayName("When no platform added, size returns zero")
        fun size_noPlatformInserted_zeroReturned() {
            // Arrange
            // Act
            val size = sut.size()
            // Assert
            assertEquals(0, size)
        }

        @Test
        @DisplayName("When multiple duplicate platforms added, size returns properly")
        fun size_multipleDuplicatePlatformsInserted_sizeReturnedProperly() {
            // Arrange
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Act
            val size = sut.size()
            // Assert
            assertEquals(2, size)
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
        @DisplayName("When platforms added one at a time, then getPlatform works correctly")
        fun addAndGetPlatform_platformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name added one at a time, then last platform of a name is fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1Dup.name)) }
            )
        }

        @Test
        @DisplayName("When multiple duplicate platforms added one at a time, then platforms are fetched correctly")
        fun addAndGetPlatform_multipleDuplicatePlatformsInsertedOneByOne_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform1Dup, sut.getPlatform(platform1Dup.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2Dup.name)) }
            )
        }

        @Test
        @DisplayName("When platforms added together as a list, then platforms are fetched correctly")
        fun addAndGetPlatform_platformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) }
            )
        }

        @Test
        @DisplayName("When platforms with same name are added together as a list, then they are fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable { assertEquals(platform1, sut.getPlatform(platform1.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2.name)) },
                Executable { assertEquals(platform2, sut.getPlatform(platform2Dup.name)) }
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
        @DisplayName("When getPlatform called with existing name, then correct platform is fetched correctly")
        fun getPlatform_existingNameUsed_correctUrlReturned() {
            // Arrange
            // Act
            val platform3Actual = sut.getPlatform(platform3.name)
            val platform4Actual = sut.getPlatform(platform4.name)
            // Assert
            assertAll(
                Executable { assertEquals(platform3, platform3Actual) },
                Executable { assertEquals(platform4, platform4Actual) }
            )
        }

        @Test
        @DisplayName("When getPlatform called with non-existing name, then null is returned")
        fun getPlatform_nonExistingNameUsed_emptyUrlReturned() {
            // Arrange
            // Act
            val platform = sut.getPlatform(platform1.name)
            // Assert
            assertEquals(null, platform)
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

        @Test
        @DisplayName("When multiple duplicate platforms added, size returns properly")
        fun size_multipleDuplicatePlatformsInserted_sizeReturnedProperly() {
            // Arrange
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Act
            val size = sut.size()
            // Assert
            assertEquals(2 + platformListPreInserted.size, size)
        }

        @Test
        @DisplayName("When getPlatformList called with no duplicated names, correct list returned according to insertion order")
        fun getPlatformList_noDuplicatedNames_listReturnedProperly() {
            // Arrange
            // Act
            val list = sut.getPlatformList()
            // Assert
            assertEquals(platformListPreInserted, list)
        }

        @Test
        @DisplayName("When getPlatformList called with duplicated names, correct list returned according to insertion order")
        fun getPlatformList_multipleDuplicatePlatformsInserted_listReturnedProperly() {
            // Arrange
            sut.add(platform1)
            sut.add(platform2Dup)
            sut.add(platform1Dup)
            sut.add(platform2)
            // Act
            val list = sut.getPlatformList()
            // Assert
            platformListPreInserted.add(platform1Dup)
            platformListPreInserted.add(platform2)
            assertEquals(platformListPreInserted, list)
        }

        @Test
        @DisplayName("When default argument not disabled explicitly, isPlatformEnabled returns true always")
        fun isPlatformEnabled_defaultArgumentNotSetExplicitly_alwaysEnabled() {
            // Arrange
            // Act
            // Assert
            assertAll(
                Executable { assertEquals(true, sut.isPlatformEnabled(platform4.name)) },
                Executable { assertEquals(true, sut.isPlatformEnabled(platform3.name)) }
            )
        }

        @Test
        @DisplayName("When added with default argument after explicit disable, isPlatformEnabled stays false")
        fun isPlatformEnabled_Platform_disabledThenAddWithDefaultArgument_isEnabledFalse() {
            // Arrange
            sut.add(platform1)
            sut.disablePlatform(platform1.name)
            sut.add(platform1Dup)
            // Act
            // Assert
            assertEquals(false, sut.isPlatformEnabled(platform1.name))
        }

        @Test
        @DisplayName("When enablePlatform used explicitly after default add, isPlatformEnabled returns true")
        fun enablePlatform_addAndThenEnabled_isEnabledTrue() {
            // Arrange
            // Act
            sut.enablePlatform(platform4.name)
            // Assert
            assertEquals(true, sut.isPlatformEnabled(platform4.name))
        }

        @Test
        @DisplayName("When enablePlatform used explicitly after explicit enable, isPlatformEnabled returns true")
        fun enablePlatform_enabledThenEnabled_isEnabledTrue() {
            // Arrange
            sut.enablePlatform(platform4.name)
            // Act
            sut.enablePlatform(platform4.name)
            // Assert
            assertEquals(true, sut.isPlatformEnabled(platform4.name))
        }

        @Test
        @DisplayName("When enablePlatform used explicitly after explicit disable, isPlatformEnabled returns true")
        fun enablePlatform_disabledThenEnabled_isEnabledTrue() {
            // Arrange
            sut.disablePlatform(platform4.name)
            // Act
            sut.enablePlatform(platform4.name)
            // Assert
            assertEquals(true, sut.isPlatformEnabled(platform4.name))
        }

        @Test
        @DisplayName("When disablePlatform used explicitly after default add, isPlatformEnabled returns false")
        fun disablePlatform_addAndThenDisabled_isEnabledFalse() {
            // Arrange
            // Act
            sut.disablePlatform(platform4.name)
            // Assert
            assertEquals(false, sut.isPlatformEnabled(platform4.name))
        }

        @Test
        @DisplayName("When disablePlatform used explicitly after explicit enable, isPlatformEnabled returns false")
        fun disablePlatform_enabledThenDisabled_isEnabledFalse() {
            // Arrange
            sut.enablePlatform(platform4.name)
            // Act
            sut.disablePlatform(platform4.name)
            // Assert
            assertEquals(false, sut.isPlatformEnabled(platform4.name))
        }

        @Test
        @DisplayName("When disablePlatform used explicitly after explicit disable, isPlatformEnabled returns false")
        fun disablePlatform_disabledThenDisabled_isEnabledFalse() {
            // Arrange
            sut.disablePlatform(platform4.name)
            // Act
            sut.disablePlatform(platform4.name)
            // Assert
            assertEquals(false, sut.isPlatformEnabled(platform4.name))
        }
    }
}
