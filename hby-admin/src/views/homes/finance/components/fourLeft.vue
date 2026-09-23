<template>
  <el-card>
    <div class="title">现金流量</div>
    <div class="content">
      <div>
        <img
          src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/1icon_xjll01.png"
          alt=""
        />
      </div>
      <div class="detail">
        <div class="type">经营活动</div>
        <div class="num">3503万元</div>
        <div class="d_title">本月现金流量</div>
      </div>
      <div class="detail">
        <div class="type"></div>
        <div class="num">138523万元</div>
        <div class="d_title">本年现金流量</div>
      </div>
    </div>
    <div class="content">
      <div>
        <img
          src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/1icon_xjll02.png"
          alt=""
        />
      </div>
      <div class="detail">
        <div class="type">供应商付款</div>
        <div class="num">3503万元</div>
        <div class="d_title">本月现金流量</div>
      </div>
      <div class="detail">
        <div class="type"></div>
        <div class="num">138523万元</div>
        <div class="d_title">本年现金流量</div>
      </div>
    </div>
    <div class="content">
      <div>
        <img
          src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/1icon_xjll03.png"
          alt=""
        />
      </div>
      <div class="detail">
        <div class="type">员工报销</div>
        <div class="num">3503万元</div>
        <div class="d_title">本月现金流量</div>
      </div>
      <div class="detail">
        <div class="type"></div>
        <div class="num">138523万元</div>
        <div class="d_title">本年现金流量</div>
      </div>
    </div>
  </el-card>
</template>

<style scoped lang="scss">
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    padding: 8px 0;
  }
  .content {
    display: flex;
    justify-content: space-around;
    padding: 10px 0;
    border-bottom: 1px solid #ebeef5;
  }
  img {
    width: 80%;
  }
  .detail {
    line-height: 20px;
  }
  .type {
    font-size: 14px;
    color: #324aa5;
    height: 20px;
  }
  .num {
    font-size: 14px;
    font-weight: 700;
    color: #000;
  }
  .d_title {
    font-size: 12px;
    color: #999;
  }
</style>
