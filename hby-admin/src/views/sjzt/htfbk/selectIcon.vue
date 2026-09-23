<template>
  <div>
    <el-popover
      v-model="visible"
      placement="bottom-start"
      trigger="click"
      width="800px"
    >
      <div v-if="visible" class="icons">
        <div
          v-for="item of imgs"
          :key="item.id"
          class="icon"
          @click="handleClickIcon(item)"
        >
          <img :src="item.url" style="width: 100px; height: 100px" />
        </div>
      </div>
      <el-input
        slot="reference"
        :placeholder="iconName ? '' : '请选择封面'"
        readonly
        :style="{ width: '256px' }"
      >
        <img
          v-if="iconName.url"
          slot="prefix"
          :src="iconName.url"
          style="width: 25px; height: 25px"
        />
      </el-input>
    </el-popover>
  </div>
</template>

<script>
  export default {
    props: {
      icon: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        visible: false,
        iconName: '',
        iconList: '',
        imgs: [
          {
            id: 1,
            url: '//pic.iask.com.cn/1i2UsmC5NCf.png',
          },
          {
            id: 2,
            url: '//pic.iask.com.cn/1caSdzLZU7zw.png',
          },
          {
            id: 3,
            url: '//pic.iask.com.cn/1caIuDOIh11a.png',
          },
          {
            id: 4,
            url: '//pic.iask.com.cn/1cdILtgEhLXY.png',
          },
          {
            id: 5,
            url: '//pic.iask.com.cn/24XqfRtmHBZ.png',
          },
          {
            id: 6,
            url: '//pic.iask.com.cn/1cbAKkj7zcEc.png',
          },
          {
            id: 7,
            url: '//pic.iask.com.cn/1cGiYpnDRxt.png',
          },
          {
            id: 8,
            url: '//pic.iask.com.cn/XTDm99dQfE8.png',
          },
          {
            id: 9,
            url: '//pic.iask.com.cn/9EbGts3JIG14.png',
          },
          {
            id: 10,
            url: '//pic.iask.com.cn/1AniBljTZvx.png',
          },
        ],
      }
    },

    created() {},
    methods: {
      handleClickIcon(item) {
        this.visible = false
        this.iconName = item
        this.$emit('getSelectIcon', item)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .icons {
    display: flex;
    flex-wrap: wrap;
    width: 620px;
    height: 200px;
    overflow: hidden;
    overflow-y: auto;
    .icon {
      width: 120px;

      margin-bottom: 10px;
      i {
        font-size: 32px;
      }
    }
  }
</style>
