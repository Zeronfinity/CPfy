package com.zeronfinity.core.entity

data class Platform(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val shortName: String,
    var isEnabled: Boolean = true,
    var notificationPriority: String = "Default"
) {
    companion object {
        fun createPlatformShortName(name: String): String = when {
            name.contains("algoge.com", ignoreCase = true) -> "Algoge"
            name.contains("atcoder.jp", ignoreCase = true) -> "AtCoder"
            name.contains("binarysearch.com", ignoreCase = true) -> "Binary Search"
            name.contains("hsin.hr/coci", ignoreCase = true) -> "COCI"
            name.contains("codility.com", ignoreCase = true) -> "Codility"
            name.contains("codechef.com", ignoreCase = true) -> "CodeChef"
            name.contains("codeforces.com/gyms", ignoreCase = true) -> "Codeforces Gym"
            name.contains("codeforces.com", ignoreCase = true) -> "Codeforces"
            name.contains("ctftime.org", ignoreCase = true) -> "CTFtime"
            name.contains("dl.gsu.by", ignoreCase = true) -> "Distance Learning"
            name.contains("e-olymp.com", ignoreCase = true) -> "E-Olymp"
            name.contains("facebook.com/hackercup", ignoreCase = true) -> "Facebook Hacker Cup"
            name.contains("google.com", ignoreCase = true) -> "Google"
            name.contains("hackerearth.com", ignoreCase = true) -> "HackerEarth"
            name.contains("acm.hdu.edu.cn", ignoreCase = true) -> "HDU"
            name.contains("neerc.ifmo.ru/school", ignoreCase = true) -> "IFMO School"
            name.contains("neerc.ifmo.ru/trains", ignoreCase = true) -> "IFMO Trains"
            name.contains("kaggle.com", ignoreCase = true) -> "Kaggle"
            name.contains("kattis.com", ignoreCase = true) -> "Kattis"
            name.contains("leetcode.com", ignoreCase = true) -> "LeetCode"
            name.contains("opencup.ru", ignoreCase = true) -> "OpenCup"
            name.contains("projecteuler.net", ignoreCase = true) -> "Project Euler"
            name.contains("russiancodecup.ru", ignoreCase = true) -> "Russian Code Cup"
            name.contains("russianaicup.ru", ignoreCase = true) -> "AI Cup"
            name.contains("contests.snarknews.info", ignoreCase = true) -> "SnarkNews"
            name.contains("spoj.com", ignoreCase = true) -> "Spoj"
            name.contains("topcoder.com", ignoreCase = true) -> "Topcoder"
            name.contains("timus.ru", ignoreCase = true) -> "Timus"
            name.contains("toph.co", ignoreCase = true) -> "Toph"
            name.contains("usaco.org", ignoreCase = true) -> "USACO"
            name.contains("uva.onlinejudge.org", ignoreCase = true) -> "UVA"
            name.contains("algorithm.contest.yandex.com", ignoreCase = true) -> "Yandex Algorithm"
            name.contains("contest.yandex.ru/CYF", ignoreCase = true) -> "Yandex CYF"
            name.contains("contest.yandex.ru", ignoreCase = true) -> "Yandex Contest"
            name.contains("yukicoder.me", ignoreCase = true) -> "Yukicoder"
            else -> name
        }
    }
}
