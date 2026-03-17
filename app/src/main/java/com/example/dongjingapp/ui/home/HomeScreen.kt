package com.example.dongjingapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

/**
 * 首页屏幕
 * 包含搜索栏、轮播图、功能入口和课程分类
 */
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)) // 浅灰色背景
    ) {
        // 顶部搜索栏
        SearchBar()
        
        // 轮播图
        Carousel()
        
        // 功能入口
        FeatureEntries(navController)
        
        // 课程分类
        CourseCategories(navController)
    }
}

/**
 * 搜索栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("搜索课程、教练") },
            leadingIcon = { Icon(painter = painterResource(id = android.R.drawable.ic_menu_search), contentDescription = "搜索") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF9FAFB),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

/**
 * 轮播图
 */
@Composable
fun Carousel() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
    ) {
        // 模拟轮播图，后续可替换为真实的轮播组件
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF3B82F6)) // 主色调
            ) {
                Text(
                    text = "推荐课程：30分钟全身燃脂",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

/**
 * 功能入口
 */
@Composable
fun FeatureEntries(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FeatureEntry(
            icon = android.R.drawable.ic_media_play,
            title = "开始训练",
            onClick = { /* TODO: 导航到训练页面 */ }
        )
        FeatureEntry(
            icon = android.R.drawable.ic_videocam,
            title = "AR模式",
            onClick = { navController.navigate("ar") }
        )
        FeatureEntry(
            icon = android.R.drawable.ic_dialog_info,
            title = "数据中心",
            onClick = { navController.navigate("stats") }
        )
        FeatureEntry(
            icon = android.R.drawable.ic_menu_gallery,
            title = "视频库",
            onClick = { navController.navigate("video/library") }
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

/**
 * 功能入口项
 */
@Composable
fun FeatureEntry(
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xFFEFF6FF), CircleShape)
                .padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color(0xFF3B82F6),
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF1F2937)
        )
    }
}

/**
 * 课程分类
 */
@Composable
fun CourseCategories(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "课程分类",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = "查看全部",
                fontSize = 14.sp,
                color = Color(0xFF3B82F6),
                modifier = Modifier.clickable {
                    navController.navigate("courses")
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        // 课程分类卡片网格
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                CourseCategoryCard(
                    title = "有氧运动",
                    count = "24",
                    color = Color(0xFFF97316), // 辅助色：橙色
                    onClick = { /* TODO: 导航到有氧运动课程 */ }
                )
            }
            item {
                CourseCategoryCard(
                    title = "力量训练",
                    count = "18",
                    color = Color(0xFF10B981), // 辅助色：绿色
                    onClick = { /* TODO: 导航到力量训练课程 */ }
                )
            }
            item {
                CourseCategoryCard(
                    title = "瑜伽",
                    count = "32",
                    color = Color(0xFFFF6B6B), // 粉红色
                    onClick = { /* TODO: 导航到瑜伽课程 */ }
                )
            }
            item {
                CourseCategoryCard(
                    title = "拉伸",
                    count = "15",
                    color = Color(0xFF8B5CF6), // 紫色
                    onClick = { /* TODO: 导航到拉伸课程 */ }
                )
            }
        }
    }
}

/**
 * 课程分类卡片
 */
@Composable
fun CourseCategoryCard(
    title: String,
    count: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "$count 课程",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}