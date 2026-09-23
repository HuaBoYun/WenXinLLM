 
<template>
  <el-card>
    <el-col :span="8">
      <div class="one-item">
        <div>
          <h2>100</h2>
          <p>重大风险事件总项数</p>
        </div>
        <div>
          <i class="el-icon-money"></i>
        </div>
      </div>
    </el-col>
    <el-col :span="8">
      <div class="one-item">
        <div>
          <h2>52</h2>
          <p>已整改重大风险事件</p>
        </div>
        <div>
          <i class="el-icon-collection"></i>
        </div>
      </div>
    </el-col>
    <el-col :span="8">
      <div class="one-item">
        <div>
          <h2>5,901.02 万</h2>
          <p>重大风险事件索赔金额</p>
        </div>
        <div>
          <i class="el-icon-umbrella"></i>
        </div>
      </div>
    </el-col>
  </el-card>
</template>

<style scoped>
  .one-item {
    display: flex;
    justify-content: space-between;
    background: white;
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #d2d2d2;
  }
  i {
    font-size: 24px;
  }
</style>