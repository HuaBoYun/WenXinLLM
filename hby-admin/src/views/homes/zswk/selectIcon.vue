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
            url: require('@/views/homes/zswk/img/1.jpg'),
          },
          {
            id: 2,
            url: require('@/views/homes/zswk/img/2.jpg'),
          },
          {
            id: 3,
            url: require('@/views/homes/zswk/img/3.jpg'),
          },
          {
            id: 4,
            url: require('@/views/homes/zswk/img/4.jpg'),
          },
          {
            id: 5,
            url: require('@/views/homes/zswk/img/5.jpg'),
          },
          {
            id: 6,
            url: require('@/views/homes/zswk/img/6.jpg'),
          },
          {
            id: 7,
            url: require('@/views/homes/zswk/img/7.jpg'),
          },
          {
            id: 8,
            url: require('@/views/homes/zswk/img/8.jpg'),
          },
          {
            id: 9,
            url: require('@/views/homes/zswk/img/9.jpg'),
          },
          {
            id: 10,
            url: require('@/views/homes/zswk/img/10.jpg'),
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
