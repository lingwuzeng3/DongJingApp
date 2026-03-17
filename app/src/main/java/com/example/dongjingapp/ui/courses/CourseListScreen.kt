package com.example.dongjingapp.ui.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.SwipeRefresh
import androidx.compose.material3.SwipeRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.dongjingapp.data.service.CourseService

/**
 * 课程列表屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(navController: androidx.navigation.NavHostController) {
    val courseService = CourseService()
    val allCourses = courseService.getCourses()
    
    // 状态管理
    val (courses, setCourses) = remember {
        mutableStateOf(allCourses)
    }
    val (isRefreshing, setIsRefreshing) = remember {
        mutableStateOf(false)
    }
    val (selectedCategory, setSelectedCategory) = remember {
        mutableStateOf("全部")
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
    
    // 刷新功能
    val onRefresh = {
        setIsRefreshing(true)
        // 模拟网络请求延迟
        kotlinx.coroutines.MainScope().launch {
            kotlinx.coroutines.delay(1000)
            applyFiltersAndSort(allCourses, selectedCategory, selectedDifficulty, selectedSort, setCourses)
            setIsRefreshing(false)
        }
    }
    
    // 应用筛选和排序
    val applyFilters = {
        setIsLoading(true)
        // 模拟筛选延迟
        kotlinx.coroutines.MainScope().launch {
            kotlinx.coroutines.delay(300)
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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
            
            // 课程网格列表
            SwipeRefresh(
                state = SwipeRefreshState(isRefreshing),
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
                        androidx.compose.material3.CircularProgressIndicator()
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
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.SearchOff,
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
                        items(courses) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn()
                            ) {
                                CourseCard(
                                    course = it,
                                    onCardClick = {
                                        navController.navigate("course/${it.id}")
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
        Box(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "分类",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    label = "全部",
                    isSelected = selectedCategory == "全部",
                    onClick = { onCategoryChange("全部") }
                )
                FilterChip(
                    label = "瑜伽",
                    isSelected = selectedCategory == "瑜伽",
                    onClick = { onCategoryChange("瑜伽") }
                )
                FilterChip(
                    label = "有氧",
                    isSelected = selectedCategory == "有氧",
                    onClick = { onCategoryChange("有氧") }
                )
                FilterChip(
                    label = "力量",
                    isSelected = selectedCategory == "力量",
                    onClick = { onCategoryChange("力量") }
                )
            }
        }
        
        // 难度筛选
        Box(
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
                FilterChip(
                    label = "全部",
                    isSelected = selectedDifficulty == "全部",
                    onClick = { onDifficultyChange("全部") }
                )
                FilterChip(
                    label = "初级",
                    isSelected = selectedDifficulty == "初级",
                    onClick = { onDifficultyChange("初级") }
                )
                FilterChip(
                    label = "中级",
                    isSelected = selectedDifficulty == "中级",
                    onClick = { onDifficultyChange("中级") }
                )
                FilterChip(
                    label = "高级",
                    isSelected = selectedDifficulty == "高级",
                    onClick = { onDifficultyChange("高级") }
                )
            }
        }
        
        // 排序选项
        Box() {
            Text(
                text = "排序",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    label = "推荐",
                    isSelected = selectedSort == "推荐",
                    onClick = { onSortChange("推荐") }
                )
                FilterChip(
                    label = "评分",
                    isSelected = selectedSort == "评分",
                    onClick = { onSortChange("评分") }
                )
                FilterChip(
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
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    androidx.compose.material3.FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
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
                
                Box(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = course.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF3B82F6)
                    )
                }
                
                Box(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "${course.difficulty} · ${course.duration}分钟",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6B7280)
                    )
                }
                
                Box(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = course.trainerName,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6B7280)
                    )
                }
                
                Box(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "⭐ ${course.rating} (${course.reviewCount})",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}