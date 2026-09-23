<template>
  <el-col :span="24" style="margin-left: -20px; overflow: auto">
    <el-timeline reverse>
      <template v-for="(item, i) in list">
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 0"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">审核节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="danger">审核拒绝</el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell">审核人员：{{ item.userName }}</p>
            <p class="timeline-cell" v-if="item.handleOpinion">
              审核意见：{{ item.handleOpinion }}
            </p>
            <p class="timeline-cell" v-if="item.signImg">
              审核签名：
              <img :src="item.signImg" class="signImg" />
            </p>
            <p class="timeline-cell2">
              <a @click="handleAtt(item)">查看附件</a>
            </p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 1"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">审核节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="success">审核通过</el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell">
              审核人员：{{ item.userName
              }}{{ item.status == 1 ? '(加签)' : '' }}
            </p>
            <p class="timeline-cell" v-if="item.handleOpinion">
              审核意见：{{ item.handleOpinion }}
            </p>
            <p class="timeline-cell" v-if="item.signImg">
              审核签名：
              <img :src="item.signImg" class="signImg" />
            </p>
            <p class="timeline-cell2">
              <a @click="handleAtt(item)">查看附件</a>
            </p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 2"
        >
          <el-card>
            <p class="timeline-cell">发起人：{{ item.userName }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 3"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">撤回节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="danger">审核撤回</el-link>
                </p>
              </el-col>
            </el-row>

            <p class="timeline-cell">撤回人员：{{ item.userName }}</p>
            <p class="timeline-cell">撤回原因：{{ item.handleOpinion }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 4"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">审核节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="danger">流程终止</el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell">执行人员：{{ item.userName }}</p>
            <p class="timeline-cell">执行动作：终止</p>
            <p class="timeline-cell">终止原因：{{ item.handleOpinion }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 5"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">审核节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="primary">流程指派</el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell">执行人员：{{ item.userName }}</p>
            <p class="timeline-cell">执行动作：指派</p>
            <p class="timeline-cell">指派人员：{{ item.operatorId }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 6"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">审核节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="primary">流程加签</el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell">执行人员：{{ item.userName }}</p>
            <p class="timeline-cell">执行动作：加签</p>
            <p class="timeline-cell">加签人员：{{ item.operatorId }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
          :timestamp="item.handleTime | toDate()"
          placement="top"
          :key="i"
          v-if="item.handleStatus == 7"
        >
          <el-card>
            <el-row>
              <el-col :span="14">
                <p class="timeline-cell">审核节点：{{ item.nodeName }}</p>
              </el-col>
              <el-col :span="10">
                <p class="timeline-cell" style="text-align: right">
                  <el-link :underline="false" type="primary">流程转审</el-link>
                </p>
              </el-col>
            </el-row>
            <p class="timeline-cell">执行人员：{{ item.userName }}</p>
            <p class="timeline-cell">执行动作：转审</p>
            <p class="timeline-cell">转审人员：{{ item.operatorId }}</p>
            <p class="timeline-cell">转审意见：{{ item.handleOpinion }}</p>
          </el-card>
        </el-timeline-item>
      </template>
      <el-timeline-item
        :timestamp="endTime | toDate()"
        placement="top"
        v-if="endTime"
      >
        <el-card>
          <p class="timeline-cell">流程结束</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    <AttListModel ref="att" />
  </el-col>
</template>

<script>
  import AttListModel from './AttListModel'
  export default {
    props: {
      list: { type: Array, default: [] },
      endTime: { type: Number, default: 0 },
    },
    components: {
      AttListModel,
    },
    name: '',
    methods: {
      handleAtt(item) {
        this.$refs.att.show(item)
      },
    },
    filters: {
      toDate(v, format) {
        format = format ? format : 'yyyy-MM-dd HH:mm'
        if (!v) return ''
        var d = v
        if (typeof v === 'string') {
          if (v.indexOf('/Date(') > -1)
            d = new Date(
              parseInt(v.replace('/Date(', '').replace(')/', ''), 10)
            )
          else
            d = new Date(
              Date.parse(v.replace(/-/g, '/').replace('T', ' ').split('.')[0])
            )
        } else {
          d = new Date(v)
        }
        var o = {
          'M+': d.getMonth() + 1,
          'd+': d.getDate(),
          'h+': d.getHours(),
          'H+': d.getHours(),
          'm+': d.getMinutes(),
          's+': d.getSeconds(),
          'q+': Math.floor((d.getMonth() + 3) / 3),
          S: d.getMilliseconds(),
        }
        if (/(y+)/.test(format)) {
          format = format.replace(
            RegExp.$1,
            (d.getFullYear() + '').substr(4 - RegExp.$1.length)
          )
        }
        for (var k in o) {
          if (new RegExp('(' + k + ')').test(format)) {
            format = format.replace(
              RegExp.$1,
              RegExp.$1.length == 1
                ? o[k]
                : ('00' + o[k]).substr(('' + o[k]).length)
            )
          }
        }
        return format
      },
    },
  }
</script>
<style lang="scss" scoped>
  .timeline-cell .el-link {
    cursor: auto !important;
  }
  .timeline-cell2 {
    cursor: pointer;
    text-align: right;
  }
</style>
