package com.example.dongjingapp.ui.courses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dongjingapp.data.model.Course
import com.example.dongjingapp.data.repository.CourseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 课程列表屏幕
 * @param initialCategory 从首页分类等入口传入的默认筛选（如：有氧、瑜伽、拉伸）
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CourseListScreen(
    navController: androidx.navigation.NavHostController,
    initialCategory: String = "全部"
) {
    val courseRepository = CourseRepository()
    val allCourses = courseRepository.getCourses()
    val coroutineScope = rememberCoroutineScope()

    // 状态管理
    val (courses, setCourses) = remember {
        mutableStateOf(allCourses)
    }
    val isRefreshing = remember {
        mutableStateOf(false)
    }
    val (selectedCategory, setSelectedCategory) = remember(initialCategory) {
        mutableStateOf(initialCategory)
    }
    val (selectedDifficulty, setSelectedDifficulty) = remember {
        mutableStateOf("全部")
    }
    val (selectedSort, setSelectedSort) = remember {
        mutableStateOf("推荐")
    }
    val (isLoading, setIsLoading) = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(selectedCategory, selectedDifficulty, selectedSort, allCourses) {
        applyFiltersAndSort(allCourses, selectedCategory, selectedDifficulty, selectedSort, setCourses)
    }

    // 创建 PullToRefresh 状态
    val pullToRefreshState = rememberPullToRefreshState()

    // 刷新功能
    val onRefresh: () -> Unit = {
        isRefreshing.value = true
        // 模拟网络请求延迟
        coroutineScope.launch {
            delay(1000)
            applyFiltersAndSort(allCourses, selectedCategory, selectedDifficulty, selectedSort, setCourses)
            isRefreshing.value = false
        }
    }

    // 应用筛选和排序
    val applyFilters = {
        setIsLoading(true)
        // 模拟筛选延迟
        coroutineScope.launch {
            delay(300)
            applyFiltersAndSort(allCourses, selectedCategory, selectedDifficulty, selectedSort, setCourses)
            setIsLoading(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("课程列表") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 筛选和排序选项
            FilterAndSortSection(
                selectedCategory = selectedCategory,
                selectedDifficulty = selectedDifficulty,
                selectedSort = selectedSort,
                onCategoryChange = {
                    setSelectedCategory(it)
                    applyFilters()
                },
                onDifficultyChange = {
                    setSelectedDifficulty(it)
                    applyFilters()
                },
                onSortChange = {
                    setSelectedSort(it)
                    applyFilters()
                }
            )

            // 使用 PullToRefreshBox 替代原有的 SwipeRefresh
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing.value,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (courses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SearchOff,
                                contentDescription = "无课程",
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF9CA3AF)
                            )
                            Text(
                                text = "没有找到符合条件的课程",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(courses) { course ->
                            // 【关键修复】：在外层套一个 Box，为 AnimatedVisibility 提供正确的作用域
                            Column {
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + scaleIn()
                                ) {
                                    CourseCard(
                                        course = course,
                                        onCardClick = {
                                            navController.navigate("course/${course.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 应用筛选和排序
 */
fun applyFiltersAndSort(
    allCourses: List<Course>,
    category: String,
    difficulty: String,
    sort: String,
    setCourses: (List<Course>) -> Unit
) {
    var filteredCourses = allCourses

    // 应用分类筛选
    if (category != "全部") {
        filteredCourses = filteredCourses.filter { it.category == category }
    }

    // 应用难度筛选
    if (difficulty != "全部") {
        filteredCourses = filteredCourses.filter { it.difficulty == difficulty }
    }

    // 应用排序
    filteredCourses = when (sort) {
        "评分" -> filteredCourses.sortedByDescending { it.rating }
        "时长" -> filteredCourses.sortedBy { it.duration }
        else -> filteredCourses // 推荐排序（默认）
    }

    setCourses(filteredCourses)
}

/**
 * 筛选和排序选项
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterAndSortSection(
    selectedCategory: String,
    selectedDifficulty: String,
    selectedSort: String,
    onCategoryChange: (String) -> Unit,
    onDifficultyChange: (String) -> Unit,
    onSortChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "全部课程",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 分类筛选
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "分类",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(
                    label = "全部",
                    isSelected = selectedCategory == "全部",
                    onClick = { onCategoryChange("全部") }
                )
                FilterChipItem(
                    label = "瑜伽",
                    isSelected = selectedCategory == "瑜伽",
                    onClick = { onCategoryChange("瑜伽") }
                )
                FilterChipItem(
                    label = "有氧",
                    isSelected = selectedCategory == "有氧",
                    onClick = { onCategoryChange("有氧") }
                )
                FilterChipItem(
                    label = "力量",
                    isSelected = selectedCategory == "力量",
                    onClick = { onCategoryChange("力量") }
                )
                FilterChipItem(
                    label = "拉伸",
                    isSelected = selectedCategory == "拉伸",
                    onClick = { onCategoryChange("拉伸") }
                )
            }
        }

        // 难度筛选
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "难度",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(
                    label = "全部",
                    isSelected = selectedDifficulty == "全部",
                    onClick = { onDifficultyChange("全部") }
                )
                FilterChipItem(
                    label = "初级",
                    isSelected = selectedDifficulty == "初级",
                    onClick = { onDifficultyChange("初级") }
                )
                FilterChipItem(
                    label = "中级",
                    isSelected = selectedDifficulty == "中级",
                    onClick = { onDifficultyChange("中级") }
                )
                FilterChipItem(
                    label = "高级",
                    isSelected = selectedDifficulty == "高级",
                    onClick = { onDifficultyChange("高级") }
                )
            }
        }

        // 排序选项
        Column {
            Text(
                text = "排序",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(
                    label = "推荐",
                    isSelected = selectedSort == "推荐",
                    onClick = { onSortChange("推荐") }
                )
                FilterChipItem(
                    label = "评分",
                    isSelected = selectedSort == "评分",
                    onClick = { onSortChange("评分") }
                )
                FilterChipItem(
                    label = "时长",
                    isSelected = selectedSort == "时长",
                    onClick = { onSortChange("时长") }
                )
            }
        }
    }
}

/**
 * 筛选芯片
 */
@Composable
fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF3B82F6),
            selectedLabelColor = Color.White,
            containerColor = Color(0xFFF3F4F6),
            labelColor = Color(0xFF6B7280)
        )
    )
}

/**
 * 课程卡片
 */
@Composable
fun CourseCard(course: Course, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onCardClick
    ) {
        Column {
            // 封面图片
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(course.coverImage)
                    .crossfade(true)
                    .build(),
                contentDescription = course.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            // 课程信息
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Text(
                    text = course.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "${course.difficulty} · ${course.duration}分钟",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = course.trainerName,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "⭐ ${course.rating} (${course.reviewCount})",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}