<template>
  <div class="personnel-analysis-card">
    <div class="card-header">
      <i class="el-icon-user"></i>
      <h4>人员分析</h4>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="personnelData" class="personnel-content">
        <!-- 人员统计概览 -->
        <div class="overview-section">
          <div class="overview-grid">
            <div class="overview-item">
              <div class="overview-value">{{ personnelData.totalEmployees || 0 }}</div>
              <div class="overview-label">总人数</div>
            </div>
            <div class="overview-item">
              <div class="overview-value">{{ personnelData.averageAge || 0 }}</div>
              <div class="overview-label">平均年龄</div>
            </div>
            <div class="overview-item">
              <div class="overview-value">{{ personnelData.turnoverRate || 0 }}%</div>
              <div class="overview-label">离职率</div>
            </div>
          </div>
        </div>

        <!-- 学历分布 -->
        <div class="education-section">
          <div class="section-title">学历分布</div>
          <div class="education-list">
            <div 
              v-for="item in personnelData.educationDistribution"
              :key="item.education"
              class="education-item"
            >
              <div class="education-label">{{ item.educationName }}</div>
              <div class="education-bar">
                <div 
                  class="education-progress"
                  :style="{ width: item.percentage + '%' }"
                ></div>
              </div>
              <div class="education-value">{{ item.count }}人</div>
            </div>
          </div>
        </div>

        <!-- 年龄分布 -->
        <div class="age-section">
          <div class="section-title">年龄分布</div>
          <div class="age-distribution">
            <div 
              v-for="item in personnelData.ageDistribution"
              :key="item.ageRange"
              class="age-item"
            >
              <div class="age-range">{{ item.ageRange }}</div>
              <div class="age-count">{{ item.count }}人</div>
              <div class="age-percentage">{{ item.percentage }}%</div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无人员分析数据</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PersonnelAnalysisCard',
  props: {
    personnelData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initEducationChart()
    })
  },
  watch: {
    personnelData: {
      handler() {
        this.$nextTick(() => {
          this.initEducationChart()
        })
      },
      deep: true
    }
  },
  methods: {
    /**
     * 初始化学历分布图表
     */
    initEducationChart() {
      if (!this.personnelData || !this.personnelData.educationDistribution) return
      
      // 这里可以使用简单的CSS进度条，或者集成ECharts
      // 当前使用CSS进度条实现
    },

    /**
     * 格式化数字
     */
    formatNumber(value) {
      if (!value) return '0'
      return Number(value).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.personnel-analysis-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
  border: 1px solid rgba(30, 60, 114, 0.1);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 6px 30px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
  }

  .card-header {
    display: flex;
    align-items: center;
    gap: 6px; // 减少图标和标题间距
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 2px solid #e8f4fd;

    i {
      font-size: 18px;
      color: #1e3c72;
    }

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #1e3c72;
      letter-spacing: 0.5px;
      flex: 1;
    }
  }

  .card-content {
    flex: 1;
    overflow-y: auto;

    .personnel-content {
      .section-title {
        font-size: 12px;
        font-weight: bold;
        color: #666;
        margin-bottom: 4px; // 减少下边距
        padding-bottom: 2px; // 减少内边距
        border-bottom: 1px solid #f0f0f0;
      }

      .overview-section {
        margin-bottom: 15px;

        .overview-grid {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 8px;

          .overview-item {
            text-align: center;
            padding: 12px;
            background: linear-gradient(135deg, #ffe0e6 0%, #ffb3ba 100%);
            border-radius: 8px;
            border: 1px solid rgba(255, 182, 193, 0.4);
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(255, 182, 193, 0.2);
            position: relative;
            overflow: hidden;

            &::before {
              content: '';
              position: absolute;
              top: 0;
              left: -100%;
              width: 100%;
              height: 100%;
              background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent);
              transition: left 0.5s;
            }

            &:hover {
              background: linear-gradient(135deg, #ffcccb 0%, #ff9999 100%);
              border-color: rgba(255, 153, 153, 0.6);
              transform: translateY(-2px);
              box-shadow: 0 4px 15px rgba(255, 153, 153, 0.3);

              &::before {
                left: 100%;
              }
            }

            .overview-value {
              font-size: 18px;
              font-weight: bold;
              color: #8b0000; // 深红色数值
              margin-bottom: 4px;
              text-shadow: 0 1px 2px rgba(255,255,255,0.5);
            }

            .overview-label {
              font-size: 11px;
              color: #a0522d; // 棕红色标签
              font-weight: 600;
              text-shadow: 0 1px 1px rgba(255,255,255,0.3);
            }
          }
        }
      }

      .education-section {
        margin-bottom: 15px;

        .section-title {
          margin-bottom: 2px !important; // 大幅减少学历分布标题下边距
        }

        .education-list {
          .education-item {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 4px; // 减少项目间距
            font-size: 11px;

            .education-label {
              min-width: 40px;
              color: #fff;
              flex-shrink: 0;
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              padding: 2px 8px;
              border-radius: 12px;
              font-size: 10px;
              font-weight: 600;
              text-align: center;
              box-shadow: 0 2px 4px rgba(102, 126, 234, 0.3);
              position: relative;
              overflow: hidden;

              &::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
                transition: left 0.5s;
              }

              &:hover::before {
                left: 100%;
              }
            }

            .education-bar {
              flex: 1;
              height: 8px;
              background: #f0f0f0;
              border-radius: 4px;
              overflow: hidden;

              .education-progress {
                height: 100%;
                background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
                transition: width 0.3s ease;
              }
            }

            .education-value {
              min-width: 30px;
              color: #333;
              font-weight: 500;
              text-align: right;
            }
          }
        }
      }

      .age-section {
        .age-distribution {
          .age-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 12px;
            margin-bottom: 6px;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            border-radius: 8px;
            font-size: 11px;
            box-shadow: 0 2px 8px rgba(240, 147, 251, 0.3);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;

            &::before {
              content: '';
              position: absolute;
              top: 0;
              left: 0;
              right: 0;
              bottom: 0;
              background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
              pointer-events: none;
            }

            &:hover {
              transform: translateY(-1px);
              box-shadow: 0 4px 12px rgba(240, 147, 251, 0.4);
            }

            .age-range {
              color: #fff;
              flex: 1;
              font-weight: 600;
              text-shadow: 0 1px 2px rgba(0,0,0,0.1);
            }

            .age-count {
              color: #fff;
              font-weight: 600;
              margin: 0 8px;
              text-shadow: 0 1px 2px rgba(0,0,0,0.1);
            }

            .age-percentage {
              color: #fff;
              font-weight: bold;
              min-width: 35px;
              text-align: right;
              text-shadow: 0 1px 2px rgba(0,0,0,0.1);
            }
          }
        }
      }
    }

    .no-data {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #999;
      font-size: 12px;
      padding: 20px;

      i {
        font-size: 16px;
      }
    }
  }
}
</style>
