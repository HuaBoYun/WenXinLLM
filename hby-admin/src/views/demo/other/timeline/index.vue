<template>
  <div class="timeline-container">
    <el-row :gutter="20">
      <el-col :lg="8" :md="12" :sm="12" :xl="8" :xs="24">
        <el-card shadow="hover">
          <template #header>
            常规风格
            <el-radio-group v-model="reverse1" class="card-header-radio">
              <el-radio :label="true">倒序</el-radio>
              <el-radio :label="false">正序</el-radio>
            </el-radio-group>
          </template>

          <el-timeline :reverse="reverse1">
            <el-timeline-item
              v-for="(item, index) in activities"
              :key="index"
              :color="item.color"
              :timestamp="item.timestamp"
            >
              <template v-if="!item.color" #dot>
                <vab-icon v-if="item.icon" :icon="item.icon" />
                <span
                  v-if="item.waver"
                  class="vab-dot"
                  :class="{
                    ['vab-dot-' + item.waver]: true,
                  }"
                >
                  <span></span>
                </span>
              </template>
              <el-card v-if="item.card" shadow="hover">
                {{ item.content }}
              </el-card>
              <template v-else>
                {{ item.content }}
              </template>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :lg="8" :md="12" :sm="12" :xl="8" :xs="24">
        <el-card shadow="hover">
          <template #header>
            卡片风格
            <el-radio-group v-model="reverse2" class="card-header-radio">
              <el-radio :label="true">倒序</el-radio>
              <el-radio :label="false">正序</el-radio>
            </el-radio-group>
          </template>

          <el-timeline :reverse="reverse2">
            <el-timeline-item
              v-for="(item, index) in activities"
              :key="index"
              :color="item.color"
              :timestamp="item.timestamp"
            >
              <template v-if="!item.color" #dot>
                <vab-icon v-if="item.icon" :icon="item.icon" />
                <span
                  v-if="item.waver"
                  class="vab-dot"
                  :class="{
                    ['vab-dot-' + item.waver]: true,
                  }"
                >
                  <span></span>
                </span>
              </template>
              <div
                class="vab-info-card"
                :class="{ ['vab-info-card-' + item.cardType]: true }"
              >
                {{ item.content }}
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'Timeline',
    data() {
      return {
        reverse1: true,
        reverse2: true,
        activities: [
          {
            content: '支持使用小清新图标',
            timestamp: '2021-04-12 20:46',
            icon: 'account-circle-line',
            cardType: 'warning',
          },
          {
            content: '支持使用小清新图标',
            timestamp: '2021-04-18 20:46',
            icon: 'archive-line',
            cardType: 'error',
          },
          {
            content: '支持自定义颜色',
            timestamp: '2021-04-03 20:46',
            color: '#13ce66',
            cardType: 'success',
          },
          {
            content: '支持默认颜色',
            timestamp: '2021-04-03 20:46',
            color: '#e4e7ed',
          },
          {
            content: '支持success闪动',
            timestamp: '2021-04-05 20:46',
            waver: 'success',
          },
          {
            content: '支持error闪动',
            timestamp: '2021-04-05 20:46',
            waver: 'error',
          },
        ],
      }
    },
    created() {},
    mounted() {},
    methods: {},
  }
</script>

<style lang="scss" scoped>
  .timeline-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .el-timeline-item__dot {
        [class*='ri'] {
          width: 12px;
          height: 12px;
          margin-left: -3px;
          background: $base-color-white;
        }

        .vab-dot {
          left: -1px;
          width: 12px;
          height: 12px;
          margin: auto !important;
        }
      }

      .el-card {
        .el-card__header {
          position: relative;

          .card-header-radio {
            position: absolute;
            top: 20px;
            right: $base-margin;
          }
        }
      }
    }

    .vab-info-card {
      position: relative;
      width: 80%;
      padding: $base-padding;
      background: #e2e2e2;
      border-radius: $base-border-radius + 2;

      &:after {
        position: absolute;
        top: 8px;
        left: -10px;
        width: 0;
        height: 0;
        overflow: hidden;
        content: '';
        border-color: #e2e2e2 transparent transparent;
        border-style: solid dashed dashed;
        border-width: 10px;
      }

      &-success {
        color: $base-color-white;
        background: $base-color-green;

        &:after {
          border-color: $base-color-green transparent transparent;
        }
      }

      &-error {
        color: $base-color-white;
        background: $base-color-red;

        &:after {
          border-color: $base-color-red transparent transparent;
        }
      }

      &-warning {
        color: $base-color-white;
        background: $base-color-orange;

        &:after {
          border-color: $base-color-orange transparent transparent;
        }
      }
    }
  }
</style>
