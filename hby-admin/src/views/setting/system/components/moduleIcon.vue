<template>
  <div>
    <el-popover
      v-model="visible"
      placement="bottom-start"
      trigger="click"
      width="420px"
    >
      <div v-if="visible" class="icons">
        <div
          v-for="(item, index) of iconList"
          :key="index"
          class="icon"
          @click="handleClickIcon(item)"
        >
          <vab-icon :icon="item" style="width: 25px; height: 25px" />
        </div>
      </div>
      <el-input
        slot="reference"
        v-model="iconName"
        :placeholder="iconName ? '' : '请选择图标'"
        readonly
      >
        <vab-icon v-if="iconName" slot="prefix" :icon="iconName" />
      </el-input>
    </el-popover>
  </div>
</template>

<script>
  const arr = [
    'apps-line',
    'bar-chart-2-line',
    'alarm-warning-line',
    'auction-line',
    'pencil-ruler-2-line',
    'archive-drawer-line',
    'scales-3-line',
    'briefcase-2-line',
    'alarm-line',
    'star-half-line',
    'anticlockwise-line',
    'database-2-fill',
    'settings-3-line',
    'money-euro-circle-line',
    'wallet-line',
    'bank-card-line',
    'shopping-cart-line',
    'file-copy-2-line',
    'calculator-line',
  ]
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
      }
    },
    watch: {
      icon: {
        handler() {
          this.iconName = this.icon
        },
        deep: true,
      },
    },
    created() {
      this.getAllIcons()
      this.iconName = this.icon
    },
    methods: {
      getAllIcons() {
        this.iconList = arr
      },
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
    width: 420px;
    height: 200px;
    overflow: hidden;
    overflow-y: auto;
    .icon {
      width: 50px;
      height: 50px;
      i {
        font-size: 32px;
      }
    }
  }
</style>
