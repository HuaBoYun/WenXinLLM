<template>
  <div class="organization-structure-card">
    <div class="card-header">
      <i class="el-icon-s-custom"></i>
      <h4>组织架构</h4>
    </div>
    
    <div class="card-content" v-loading="loading">
      <div v-if="displayData" class="organization-content">
        <!-- 组织架构树形图 -->
        <div class="org-tree-container">
          <div class="org-tree" ref="orgTree">
            <div class="org-node root-node">
              <div class="node-content">
                <div class="node-title">{{ displayData.rootDepartment.departmentName }}</div>
                <div class="node-info">{{ displayData.rootDepartment.employeeCount }}人</div>
              </div>

              <!-- 子部门 -->
              <div v-if="displayData.subDepartments && displayData.subDepartments.length > 0" class="children-nodes">
                <div
                  v-for="dept in displayData.subDepartments"
                  :key="dept.departmentId"
                  class="org-node child-node"
                >
                  <div class="node-content">
                    <div class="node-title">{{ dept.departmentName }}</div>
                    <div class="node-info">{{ dept.employeeCount }}人</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 组织统计 -->
        <div class="org-stats">
          <div class="stat-item">
            <div class="stat-value">{{ displayData.totalDepartments || 0 }}</div>
            <div class="stat-label">部门数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ displayData.totalEmployees || 0 }}</div>
            <div class="stat-label">总人数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ displayData.managementLevels || 0 }}</div>
            <div class="stat-label">管理层级</div>
          </div>
        </div>
      </div>
      
      <div v-else class="no-data">
        <i class="el-icon-info"></i>
        <span>暂无组织架构数据</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrganizationStructureCard',
  props: {
    organizationData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    /**
     * 显示数据 - 优先使用真实数据，否则使用模拟数据
     */
    displayData() {
      if (this.organizationData) {
        return this.organizationData
      }

      // 返回模拟数据
      return {
        rootDepartment: {
          departmentId: '1',
          departmentName: '示例云科技有限公司',
          employeeCount: 1250
        },
        subDepartments: [
          { departmentId: '2', departmentName: '技术部', employeeCount: 450 },
          { departmentId: '3', departmentName: '产品部', employeeCount: 180 },
          { departmentId: '4', departmentName: '市场部', employeeCount: 120 },
          { departmentId: '5', departmentName: '销售部', employeeCount: 200 },
          { departmentId: '6', departmentName: '人事部', employeeCount: 80 },
          { departmentId: '7', departmentName: '财务部', employeeCount: 60 },
          { departmentId: '8', departmentName: '行政部', employeeCount: 90 },
          { departmentId: '9', departmentName: '法务部', employeeCount: 70 }
        ],
        totalDepartments: 9,
        totalEmployees: 1250,
        managementLevels: 4
      }
    }
  },
  mounted() {
    // 组件挂载后可以进行一些初始化操作
  },
  methods: {
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
.organization-structure-card {
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

  // 全屏状态下的适配
  .enterprise-profile-fullscreen & {
    height: calc(100vh - 120px);

    .org-tree-container {
      max-height: calc(100vh - 300px);
    }
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
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .organization-content {
      flex: 1;
      display: flex;
      flex-direction: column;

      .org-tree-container {
        flex: 1;
        overflow-y: auto;
        overflow-x: hidden;
        margin-bottom: 12px;
        max-height: 200px;
        min-height: 120px;

        // 自定义滚动条样式
        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 3px;
        }

        &::-webkit-scrollbar-thumb {
          background: #c1c1c1;
          border-radius: 3px;

          &:hover {
            background: #a8a8a8;
          }
        }

        // 响应式设计
        @media (max-width: 768px) {
          max-height: 150px;
        }

        @media (min-width: 1200px) {
          max-height: 250px;
        }

        .org-tree {
          .org-node {
            margin-bottom: 8px;

            .node-content {
              background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
              border: 2px solid rgba(102, 126, 234, 0.3);
              border-radius: 8px;
              padding: 10px;
              text-align: center;
              position: relative;
              transition: all 0.3s ease;
              cursor: pointer;
              box-shadow: 0 3px 8px rgba(102, 126, 234, 0.2);
              overflow: hidden;

              &::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
                transition: left 0.5s;
              }

              &:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);

                &::before {
                  left: 100%;
                }
              }

              .node-title {
                font-size: 12px;
                font-weight: bold;
                color: #fff;
                margin-bottom: 2px;
                text-shadow: 0 1px 2px rgba(0,0,0,0.1);
              }

              .node-info {
                font-size: 10px;
                color: rgba(255,255,255,0.9);
                text-shadow: 0 1px 2px rgba(0,0,0,0.1);
              }
            }

            &.root-node {
              .node-content {
                background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
                color: white;
                border-color: #1e3c72;

                .node-title {
                  color: white;
                }

                .node-info {
                  color: rgba(255, 255, 255, 0.8);
                }
              }
            }

            .children-nodes {
              display: grid;
              grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
              gap: 6px;
              margin-top: 8px;
              padding-left: 10px;
              border-left: 2px solid #e9ecef;

              .child-node {
                margin-bottom: 0;

                .node-content {
                  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
                  border: 2px solid rgba(252, 182, 159, 0.4);
                  box-shadow: 0 2px 6px rgba(252, 182, 159, 0.3);

                  &:hover {
                    background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
                    border-color: rgba(255, 154, 158, 0.5);
                    box-shadow: 0 4px 12px rgba(255, 154, 158, 0.4);
                  }

                  .node-title {
                    color: #8b4513 !important;
                    font-weight: bold;
                    text-shadow: 0 1px 1px rgba(255,255,255,0.5);
                  }

                  .node-info {
                    color: #a0522d !important;
                    text-shadow: 0 1px 1px rgba(255,255,255,0.5);
                  }

                  &:hover {
                    background: #f0f9ff;
                    border-color: #1e3c72;

                    .node-title {
                      color: #1e3c72 !important;
                    }
                  }
                }
              }
            }
          }
        }
      }

      .org-stats {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 8px;
        padding-top: 8px;
        border-top: 1px solid #e8e8e8;

        .stat-item {
          text-align: center;
          padding: 6px;
          background: #f8f9fa;
          border-radius: 4px;

          .stat-value {
            font-size: 16px;
            font-weight: bold;
            color: #1e3c72;
            margin-bottom: 2px;
          }

          .stat-label {
            font-size: 10px;
            color: #666;
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
