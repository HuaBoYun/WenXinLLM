<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="6">
        <div class="contanor c1">
          <div class="title">
            <div>订单情况</div>
            <div class="t_d">
              <div @click="selectM(1)" :class="activeKey == 1 ? 'light' : ''">
                上月
              </div>
              <div @click="selectM(2)" :class="activeKey == 2 ? 'light' : ''">
                本月
              </div>
              <div @click="selectM(3)" :class="activeKey == 3 ? 'light' : ''">
                本年
              </div>
            </div>
          </div>
          <div class="content">
            <div>
              <div>
                <span>2384.34</span>
                <span class="pp">万元</span>
              </div>
              <div>销售合同</div>
            </div>
            <div>
              <div>
                <span>623.34</span>
                <span class="pp">万元</span>
              </div>
              <div>采购合同</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="contanor c2">
          <div class="title">
            <div>订单情况</div>
            <div class="t_d">
              <div @click="selectM(1)" :class="activeKey == 1 ? 'light' : ''">
                上月
              </div>
              <div @click="selectM(2)" :class="activeKey == 2 ? 'light' : ''">
                本月
              </div>
              <div @click="selectM(3)" :class="activeKey == 3 ? 'light' : ''">
                本年
              </div>
            </div>
          </div>
          <div class="content">
            <div>
              <div>
                <span>2384.34</span>
                <span class="pp">万元</span>
              </div>
              <div>销售合同</div>
            </div>
            <div>
              <div>
                <span>623.34</span>
                <span class="pp">万元</span>
              </div>
              <div>采购合同</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="contanor c3">
          <div class="title">
            <div>供应商情况</div>
          </div>
          <div class="content">
            <div>
              <div>
                <span>1003</span>
                <span class="pp">家</span>
              </div>
              <div class="ppp">
                <div>A级 48</div>
                <div>B级 362</div>
                <div>C级 623</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="contanor c4">
          <div class="title">
            <div>用印情况</div>
            <div class="t_d">
              <div @click="selectM(1)" :class="activeKey == 1 ? 'light' : ''">
                上月
              </div>
              <div @click="selectM(2)" :class="activeKey == 2 ? 'light' : ''">
                本月
              </div>
              <div @click="selectM(3)" :class="activeKey == 3 ? 'light' : ''">
                本年
              </div>
            </div>
          </div>
          <div class="content">
            <div>
              <div>
                <span>232</span>
                <span class="pp">份申请</span>
              </div>
              <div class="ppp">
                <div>已签署 148</div>
                <div>未签署 362</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        activeKey: '',
      }
    },
    methods: {
      selectM(e) {
        this.activeKey = e
      },
    },
  }
</script>

<style scoped lang="scss">
  .contanor {
    background: #99b5d4;
    /* border: 1px solid #ebeef5; */
    padding: 20px 25px;
    border-radius: 10px;
    color: #fff;
  }

  .c1 {
    background: #92b6d6;
  }
  .c2 {
    background: #2f84c6;
  }
  .c3 {
    background: #65a4d4;
  }
  .c4 {
    background: #2abcbb;
  }

  .contanor .title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0px 0 10px 0;
    border-bottom: 1px solid #ebeef5;
  }
  .t_d {
    display: flex;
    width: 45%;
    justify-content: space-between;
    :hover {
      cursor: pointer;
    }
  }
  .content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    span {
      font-size: 20px;
      font-weight: 600;
    }
    .pp {
      font-size: 12px;
    }
  }
  .light {
    color: #409eff;
  }
  .ppp {
    display: flex;
    width: 120%;
    justify-content: space-between;
  }
</style>
