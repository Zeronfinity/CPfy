package com.zeronfinity.core.entity

data class Platform(
    val name: String,
    val imageUrl: String,
    val shortName: String,
    var isEnabled: Boolean = true
)

fun createPlatformShortName(name: String): String = when {
    name.contains("atcoder.jp", ignoreCase = true) -> "AtCoder"
    name.contains("binarysearch.com", ignoreCase = true) -> "Binary Search"
    name.contains("codility.com", ignoreCase = true) -> "Codility"
    name.contains("codechef.com", ignoreCase = true) -> "CodeChef"
    name.contains("codeforces.com", ignoreCase = true) -> "Codeforces"
    name.contains("ctftime.org", ignoreCase = true) -> "CTFtime"
    name.contains("dl.gsu.by", ignoreCase = true) -> "Distance Learning"
    name.contains("e-olymp.com", ignoreCase = true) -> "E-Olymp"
    name.contains("hackerearth.com", ignoreCase = true) -> "HackerEarth"
    name.contains("kaggle.com", ignoreCase = true) -> "Kaggle"
    name.contains("leetcode.com", ignoreCase = true) -> "LeetCode"
    name.contains("opencup.ru", ignoreCase = true) -> "OpenCup"
    name.contains("projecteuler.net", ignoreCase = true) -> "Project Euler"
    name.contains("russianaicup.ru", ignoreCase = true) -> "AI Cup"
    name.contains("spoj.com", ignoreCase = true) -> "Spoj"
    name.contains("topcoder.com", ignoreCase = true) -> "Topcoder"
    name.contains("toph.co", ignoreCase = true) -> "Toph"
    name.contains("yandex.ru", ignoreCase = true) -> "Yandex"
    name.contains("yukicoder.me", ignoreCase = true) -> "Yukicoder"
    else -> name
}
