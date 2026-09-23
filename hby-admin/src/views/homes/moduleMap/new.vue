<template>
  <div class="content">
    <el-card class="left">
      <div>
        <el-input placeholder="搜索" v-model="input4">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
      </div>
      <ul>
        <li
          v-for="item in list"
          :key="item.uniqueIdentification"
          @click="handleChange(item)"
          :class="item.uniqueIdentification == activityKey ? 'activity' : ''"
        >
          {{ item.projectName }}
        </li>
      </ul>
    </el-card>
    <el-card class="right">
      <div class="r" v-for="(item, index) in rightList" :key="index">
        <div class="r_left">{{ item.name }}</div>
        <div class="r_right">
          <span
            class="detail"
            v-for="(item1, index1) in item.children"
            :key="index1"
            @click="hanldeJump(item1, item)"
          >
            {{ item1.name }}
          </span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
  import { getModuleList } from '@/api/setting/system'
  import { getAuthListForUser } from '@/api/setting/auths'
  export default {
    data() {
      return {
        list: [],
        rightList: [],
        activityKey: '',
      }
    },
    async created() {
      const res = await getModuleList({})
      if (res.code == 200) {
        this.list = res.data
      }
      this.handleChange(res.data[0])
    },
    methods: {
      async handleChange(item) {
        const type = item.uniqueIdentification
        this.activityKey = item.uniqueIdentification
        const res = await getAuthListForUser({ moduleType: type })
        this.$nextTick(() => {
          this.rightList = res.data.rightList || []
        })
      },
      hanldeJump(a, b) {
        const url = '/' + b.path + '/' + a.path
        this.$router.push(url)
      },
    },
  }
</script>

<style scoped lang="scss">
  .content {
    display: flex;
    justify-content: row;
  }
  .left {
    width: 25%;
  }
  .right {
    flex: 1;
    margin-left: 10px;
  }
  li {
    margin-bottom: 10px;
    &:hover {
      cursor: pointer;
      color: #4285f4;
    }
  }
  .r {
    display: flex;
    justify-content: row;
    margin-bottom: 20px;
  }
  .r_left {
    width: 15%;
    color: #4285f4;
    font-weight: bold;
    font-size: 16px;
    &::before {
      width: 1px;
      content: '';
      height: 80%;
      border: 1px solid #4285f4;
      margin-right: 5px;
    }
  }
  .r_right {
    flex: 1;
  }
  .detail {
    display: inline-block;
    width: 20%;
    margin-bottom: 10px;
    &:hover {
      cursor: pointer;
      color: #4285f4;
    }
  }
  .activity {
    color: #4285f4;
  }
</style>
