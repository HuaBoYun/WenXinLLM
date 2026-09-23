<template>
  <el-card>
    <div class="title">财务提醒</div>
    <div class="content">
      <div class="top">
        <div class="img">
          <img
            src="https://enterprise.e-cology.com.cn/cloudstore/release/3d14457595ef4785a1ee406c5650c01d/resources/yujing001.png"
            alt=""
          />
        </div>
        <div class="text">
          <div>财务提醒</div>
          <div>当前存在财务提醒</div>
        </div>
      </div>
      <div class="num">12</div>
    </div>
    <div>
      <el-collapse v-model="activeNames" accordion>
        <el-collapse-item title="付款提醒" name="1">
          <template slot="title">
            <div>付款提醒</div>
            <button>12</button>
          </template>
          <ul>
            <li>保洁服务合同付款提醒</li>
            <li>保洁服务合同付款提醒</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="发票到期提醒" name="2">
          <template slot="title">
            <div>发票到期提醒</div>
            <button>4</button>
          </template>
          <ul>
            <li>保洁服务合同付款提醒</li>
            <li>保洁服务合同付款提醒</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="收票提醒" name="3">
          <template slot="title">
            <div>收票提醒</div>
            <button>4</button>
          </template>
          <ul>
            <li>保洁服务合同付款提醒</li>
            <li>保洁服务合同付款提醒</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="供应商对账提醒" name="4">
          <template slot="title">
            <div>供应商对账提醒</div>
            <button>4</button>
          </template>

          <ul>
            <li>保洁服务合同付款提醒</li>
            <li>保洁服务合同付款提醒</li>
          </ul>
        </el-collapse-item>
      </el-collapse>
    </div>
  </el-card>
</template>

<script>
  export default {
    data() {
      return {
        activeNames: ['1'],
      }
    },
  }
</script>

<style scoped lang="scss">
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    padding: 10px 0;
  }
  .top {
    display: flex;
    align-items: center;
  }
  .img {
    width: 40px;
    margin-right: 20px;
    img {
      width: 100%;
      vertical-align: middle;
    }
  }
  .text {
    vertical-align: middle;
  }
  .content {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid #e6e6e6;
  }
  .num {
    font-size: 20px;
    font-weight: 700;
    color: #324aa5;
    padding: 10px 0;
  }
  .d_title {
    display: flex;
    justify-content: space-between;
    padding: 10px 25px;
    .ddd {
      &:before {
        content: '';
        display: inline-block;
        width: 10px;
        height: 10px;
        background: #324aa5;
        margin-right: 10px;
      }
    }
  }
  li {
    color: #999;
  }
  button {
    background: #ebf3fd !important;
    width: 40px;
    height: 20px;
    color: #324aa5;
    border: none;
    border-radius: 50%;
    overflow: hidden;
  }
</style>
