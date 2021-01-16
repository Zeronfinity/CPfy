package com.zeronfinity.cpfy.framework.implementations

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.cpfy.framework.db.dao.PlatformDao
import com.zeronfinity.cpfy.framework.db.entity.PlatformEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class PlatformDbTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    private val platform1 = Platform(1, "name1", "imageUrl1", "name1")
    private val platform2 = Platform(2, "name2", "imageUrl2", "name2")
    private val platform1Dup = Platform(1, "name1", "imageUrl3", "name1")
    private val platform2Dup = Platform(2, "name2", "imageUrl4", "name2")
    private val platform3 = Platform(3, "name3", "imageUrl3", "name3")
    private val platform4 = Platform(4, "name4", "imageUrl4", "name4")
    private val platformListPreInserted = arrayListOf(platform4, platform3)

    private val sut = PlatformDb(testDispatcher, PlatformDaoTd())

    @Nested
    @DisplayName("Given no platforms added before")
    inner class EmptyPlatformMap {
        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added one at a time, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name added one at a time, then image of last one is fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
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
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1Dup.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name are added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added one at a time, then getPlatform works correctly")
        fun addAndGetPlatform_platformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name added one at a time, then last platform of a name is fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
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
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1Dup.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added together as a list, then platforms are fetched correctly")
        fun addAndGetPlatform_platformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name are added together as a list, then they are fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When no platform added, size returns zero")
        fun getPlatformList_noPlatformInserted_emptyListReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val list = sut.getPlatformList().first()
                // Assert
                assertEquals(0, list.size)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When no platform added, size returns zero")
        fun size_noPlatformInserted_zeroReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val size = sut.size()
                // Assert
                assertEquals(0, size)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When multiple duplicate platforms added, size returns properly")
        fun size_multipleDuplicatePlatformsInserted_sizeReturnedProperly() {
            testDispatcher.runBlockingTest {
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
    }

    @Nested
    @DisplayName("Given some platforms added before")
    inner class NonEmptyPlatformMap {
        init {
            println("NonEmptyPlatformMap init")
            sut.add(platformListPreInserted)
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added one at a time, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name added one at a time, then image of last one is fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedOneByOne_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
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
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup.imageUrl,
                            sut.getImageUrl(platform1Dup.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_platformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name are added together as a list, then their images are fetched correctly")
        fun addAndGetImageUrl_duplicatePlatformsInsertedTogether_imageUrlsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1.imageUrl,
                            sut.getImageUrl(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2.imageUrl,
                            sut.getImageUrl(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added one at a time, then getPlatform works correctly")
        fun addAndGetPlatform_platformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform2)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name added one at a time, then last platform of a name is fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedOneByOne_fetchedCorrectly() {
            // Arrange
            // Act
            sut.add(platform1)
            sut.add(platform1Dup)
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
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
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1Dup,
                            sut.getPlatform(platform1Dup.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2Dup.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms added together as a list, then platforms are fetched correctly")
        fun addAndGetPlatform_platformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When platforms with same name are added together as a list, then they are fetched correctly")
        fun addAndGetPlatform_duplicatePlatformsInsertedTogether_platformsFetchedCorrectly() {
            // Arrange
            // Act
            sut.add(arrayListOf(platform1, platform2Dup, platform2))
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform1,
                            sut.getPlatform(platform1.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            platform2,
                            sut.getPlatform(platform2.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest { assertEquals(platform2, sut.getPlatform(platform2Dup.id)) }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getImageUrl called with existing name, then correct image url is fetched correctly")
        fun getImageUrl_existingNameUsed_correctUrlReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val imageUrl3 = sut.getImageUrl(platform3.id)
                val imageUrl4 = sut.getImageUrl(platform4.id)
                // Assert
                assertAll(
                    Executable {
                        testDispatcher.runBlockingTest {
                            assertEquals(
                                platform3.imageUrl,
                                imageUrl3
                            )
                        }
                    },
                    Executable {
                        testDispatcher.runBlockingTest {
                            assertEquals(
                                platform4.imageUrl,
                                imageUrl4
                            )
                        }
                    }
                )
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getImageUrl called with non-existing name, then null is returned")
        fun getImageUrl_nonExistingNameUsed_emptyUrlReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val imageUrl1 = sut.getImageUrl(platform1.id)
                // Assert
                assertEquals(null, imageUrl1)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getPlatform called with existing name, then correct platform is fetched correctly")
        fun getPlatform_existingNameUsed_correctUrlReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val platform3Actual = sut.getPlatform(platform3.id)
                val platform4Actual = sut.getPlatform(platform4.id)
                // Assert
                assertAll(
                    Executable { testDispatcher.runBlockingTest { assertEquals(platform3, platform3Actual) } },
                    Executable { testDispatcher.runBlockingTest { assertEquals(platform4, platform4Actual) } }
                )
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getPlatform called with non-existing name, then null is returned")
        fun getPlatform_nonExistingNameUsed_emptyUrlReturned() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val platform = sut.getPlatform(platform1.id)
                // Assert
                assertEquals(null, platform)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When all platforms are removed, then no data exists")
        fun removeAll_noDataExists() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.removeAll()
                // Assert
                assertAll(
                    Executable {
                        testDispatcher.runBlockingTest {
                            assertEquals(
                                null,
                                sut.getImageUrl(platform3.id)
                            )
                        }
                    },
                    Executable {
                        testDispatcher.runBlockingTest {
                            assertEquals(
                                null,
                                sut.getImageUrl(platform4.id)
                            )
                        }
                    }
                )
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When multiple duplicate platforms added, size returns properly")
        fun size_multipleDuplicatePlatformsInserted_sizeReturnedProperly() {
            testDispatcher.runBlockingTest {
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
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getPlatformList called with no duplicated names, correct list returned according to insertion order")
        fun getPlatformList_noDuplicatedNames_listReturnedProperly() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                val list = sut.getPlatformList().first()
                // Assert
                assertEquals(platformListPreInserted, list)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When getPlatformList called with duplicated names, correct list returned according to insertion order")
        fun getPlatformList_multipleDuplicatePlatformsInserted_listReturnedProperly() {
            testDispatcher.runBlockingTest {
                // Arrange
                sut.add(platform1)
                sut.add(platform2Dup)
                sut.add(platform1Dup)
                sut.add(platform2)
                // Act
                val list = sut.getPlatformList().first()
                // Assert
                platformListPreInserted.add(platform1Dup)
                platformListPreInserted.add(platform2)
                assertEquals(platformListPreInserted, list)
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When default argument not disabled explicitly, isPlatformEnabled returns true always")
        fun isPlatformEnabled_defaultArgumentNotSetExplicitly_alwaysEnabled() {
            // Arrange
            // Act
            // Assert
            assertAll(
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            true,
                            sut.isPlatformEnabled(platform4.id)
                        )
                    }
                },
                Executable {
                    testDispatcher.runBlockingTest {
                        assertEquals(
                            true,
                            sut.isPlatformEnabled(platform3.id)
                        )
                    }
                }
            )
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When added with default argument after explicit disable, isPlatformEnabled stays false")
        fun isPlatformEnabled_Platform_disabledThenAddWithDefaultArgument_isEnabledFalse() {
            testDispatcher.runBlockingTest {
                // Arrange
                sut.add(platform1)
                sut.disablePlatform(platform1.id)
                sut.add(platform1Dup)
                // Act
                // Assert
                assertEquals(false, sut.isPlatformEnabled(platform1.id))
            }
        }

        @Test
        @DisplayName("When enablePlatform used explicitly after default add, isPlatformEnabled returns true")
        fun enablePlatform_addAndThenEnabled_isEnabledTrue() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.enablePlatform(platform4.id)
                // Assert
                assertEquals(true, sut.isPlatformEnabled(platform4.id))
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When enablePlatform used explicitly after explicit enable, isPlatformEnabled returns true")
        fun enablePlatform_enabledThenEnabled_isEnabledTrue() {
            testDispatcher.runBlockingTest {
                // Arrange
                sut.enablePlatform(platform4.id)
                // Act
                sut.enablePlatform(platform4.id)
                // Assert
                assertEquals(true, sut.isPlatformEnabled(platform4.id))
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When enablePlatform used explicitly after explicit disable, isPlatformEnabled returns true")
        fun enablePlatform_disabledThenEnabled_isEnabledTrue() {
            testDispatcher.runBlockingTest {
                // Arrange
                sut.disablePlatform(platform4.id)
                // Act
                sut.enablePlatform(platform4.id)
                // Assert
                assertEquals(true, sut.isPlatformEnabled(platform4.id))
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When disablePlatform used explicitly after default add, isPlatformEnabled returns false")
        fun disablePlatform_addAndThenDisabled_isEnabledFalse() {
            testDispatcher.runBlockingTest {
                // Arrange
                // Act
                sut.disablePlatform(platform4.id)
                // Assert
                assertEquals(false, sut.isPlatformEnabled(platform4.id))
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When disablePlatform used explicitly after explicit enable, isPlatformEnabled returns false")
        fun disablePlatform_enabledThenDisabled_isEnabledFalse() {
            testDispatcher.runBlockingTest {
                // Arrange
                sut.enablePlatform(platform4.id)
                // Act
                sut.disablePlatform(platform4.id)
                // Assert
                assertEquals(false, sut.isPlatformEnabled(platform4.id))
            }
        }

        @ExperimentalCoroutinesApi
        @Test
        @DisplayName("When disablePlatform used explicitly after explicit disable, isPlatformEnabled returns false")
        fun disablePlatform_disabledThenDisabled_isEnabledFalse() {
            testDispatcher.runBlockingTest {
                // Arrange
                sut.disablePlatform(platform4.id)
                // Act
                sut.disablePlatform(platform4.id)
                // Assert
                assertEquals(false, sut.isPlatformEnabled(platform4.id))
            }
        }
    }

    class PlatformDaoTd : PlatformDao {
        private val platformMap = mutableMapOf<Int, PlatformEntity>()

        override suspend fun deleteAll() {
            platformMap.clear()
        }

        override suspend fun getImageUrl(id: Int): String? {
            return platformMap[id]?.imageUrl
        }

        override suspend fun getPlatform(id: Int): PlatformEntity? {
            return platformMap[id]
        }

        override fun getPlatformAll(): Flow<List<PlatformEntity>> {
            return flow {
                emit(platformMap.values.toList())
            }
        }

        override suspend fun getRowCount(): Int? {
            return platformMap.size
        }

        override suspend fun isEnabled(id: Int): Boolean? {
            println("isEnabled() -> id = [$id], isEnabled = [${platformMap[id]?.isEnabled}], platformMap = [$platformMap]")
            return platformMap[id]?.isEnabled
        }

        override suspend fun setEnabled(id: Int, isEnabled: Boolean) {
            println("setIsEnabled() -> id = [$id], isEnabled = [$isEnabled], platformMap = [$platformMap]")
            platformMap[id]?.let {
                println("id = [$id], isEnabled = [$isEnabled]")
                it.isEnabled = isEnabled
            }
        }

        override suspend fun setEnabledAll(isEnabled: Boolean) {
            platformMap.map {
                it.value.isEnabled = isEnabled
            }
        }

        override suspend fun insert(vararg obj: PlatformEntity) {
            for (platform in obj) {
                platformMap[platform.id] = platform
            }
        }
    }
}
