<template>
  <div class="system-log-container" id="sjqktjb">
    <div style="text-align: right">
      <el-button type="success" @click="handleExport">导出</el-button>
    </div>
    <div class="table-content">
      <h2 class="table-title">单位内部审计业务情况表</h2>
      <table class="xwtable">
        <thead>
          <tr class="no-border-tr">
            <td style="text-align: left" colspan="2"></td>
            <td></td>
            <td style="text-align: center">表 号：</td>
            <td style="text-align: right">内审统02表</td>
          </tr>
          <tr class="no-border-tr">
            <td style="text-align: left" colspan="2"></td>
            <td></td>
            <td style="text-align: center">制定机关：</td>
            <td style="text-align: right">审计署</td>
          </tr>
          <tr class="no-border-tr">
            <td style="text-align: left" colspan="2"></td>
            <td></td>
            <td style="text-align: center">批准机关：</td>
            <td style="text-align: right">国家统计局</td>
          </tr>
          <tr class="no-border-tr">
            <td style="text-align: left" colspan="2">统一社会信用代码：</td>
            <td></td>
            <td style="text-align: center">批准文号：</td>
            <td style="text-align: right">国统制[2020]156号</td>
          </tr>
          <tr class="no-border-tr">
            <td style="text-align: left" colspan="2">单位详细名称：</td>
            <td style="text-align: center">20**年*半年</td>
            <td style="text-align: center">有效期至：</td>
            <td style="text-align: right">2023年11月</td>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td style="text-align: center" colspan="2">指标名称</td>
            <td style="text-align: center">代码</td>
            <td style="text-align: center">计量单位</td>
            <td style="text-align: center">数量</td>
          </tr>
          <tr>
            <td style="text-align: center" colspan="2">甲</td>
            <td style="text-align: center">乙</td>
            <td style="text-align: center">丙</td>
            <td style="text-align: center">1</td>
          </tr>
          <tr>
            <td colspan="2">一、审计项目</td>
            <td style="text-align: center">020100</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.sjxm }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：贯彻落实国家重大政策措施审计</td>
            <td style="text-align: center">020101</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.zdzccssj }}</td>
          </tr>
          <tr>
            <td colspan="2">财政财务收支审计</td>
            <td style="text-align: center">020102</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.cwszsj }}</td>
          </tr>
          <tr>
            <td colspan="2">固定资产投资审计</td>
            <td style="text-align: center">020103</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.gdzc }}</td>
          </tr>
          <tr>
            <td colspan="2">内部控制和风险管理审计</td>
            <td style="text-align: center">020104</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.fxgl }}</td>
          </tr>
          <tr>
            <td colspan="2">经济责任审计</td>
            <td style="text-align: center">020105</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.zrsj }}</td>
          </tr>
          <tr>
            <td colspan="2">信息系统审计</td>
            <td style="text-align: center">020106</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.xtsj }}</td>
          </tr>
          <tr>
            <td colspan="2">境外审计</td>
            <td style="text-align: center">020107</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.jwsj }}</td>
          </tr>
          <tr>
            <td colspan="2">其他</td>
            <td style="text-align: center">020108</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.qtsj }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：委托外包项目</td>
            <td style="text-align: center">020111</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.wbsj }}</td>
          </tr>
          <tr>
            <td colspan="2">二、本填报周期内部审计工作量</td>
            <td style="text-align: center">020200</td>
            <td style="text-align: center">人日</td>
            <td style="text-align: center">{{ form.sjgzl }}</td>
          </tr>
          <tr>
            <td colspan="2">三、审计发现问题金额</td>
            <td style="text-align: center">020300</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.wtje }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：绩效类问题金额</td>
            <td style="text-align: center">020310</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.jxlwtje }}</td>
          </tr>
          <tr>
            <td colspan="2">合规性问题金额</td>
            <td style="text-align: center">020320</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.hgxwtje }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：会计核算方面</td>
            <td style="text-align: center">020321</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.kjhs }}</td>
          </tr>
          <tr>
            <td colspan="2">违规使用资金</td>
            <td style="text-align: center">020322</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.wgsy }}</td>
          </tr>
          <tr>
            <td colspan="2">截留、沉淀资金</td>
            <td style="text-align: center">020323</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.jlcd }}</td>
          </tr>
          <tr>
            <td colspan="2">损失浪费</td>
            <td style="text-align: center">020324</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.sslf }}</td>
          </tr>
          <tr>
            <td colspan="2">挪用资金</td>
            <td style="text-align: center">020325</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.nyzj }}</td>
          </tr>
          <tr>
            <td colspan="2">偷漏税费</td>
            <td style="text-align: center">020326</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.tsls }}</td>
          </tr>
          <tr>
            <td colspan="2">违规取得收入</td>
            <td style="text-align: center">020327</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.wgqd }}</td>
          </tr>
          <tr>
            <td colspan="2">其他</td>
            <td style="text-align: center">020328</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.wtqtje }}</td>
          </tr>
          <tr>
            <td colspan="2">四、审计发现问题个数</td>
            <td style="text-align: center">020400</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.wtzs }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：金额类问题个数</td>
            <td style="text-align: center">020420</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.jelzs }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：国家政策措施落实方面</td>
            <td style="text-align: center">020421</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.gjzc }}</td>
          </tr>
          <tr>
            <td colspan="2">发展规划与战略决策方面</td>
            <td style="text-align: center">020422</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.fzgh }}</td>
          </tr>
          <tr>
            <td colspan="2">内部控制与风险管理方面</td>
            <td style="text-align: center">020423</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.nbkz }}</td>
          </tr>
          <tr>
            <td colspan="2">其他</td>
            <td style="text-align: center">020424</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.wtqt }}</td>
          </tr>
          <tr>
            <td colspan="2">五、审计发现问题整改（金额类）</td>
            <td style="text-align: center">020500</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.wtzgjel }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：调整会计账目</td>
            <td style="text-align: center">020501</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.tzzm }}</td>
          </tr>
          <tr>
            <td colspan="2">收回资金</td>
            <td style="text-align: center">020502</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.shzj }}</td>
          </tr>
          <tr>
            <td colspan="2">挽回损失</td>
            <td style="text-align: center">020503</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.whss }}</td>
          </tr>
          <tr>
            <td colspan="2">归还原资金渠道</td>
            <td style="text-align: center">020504</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.ghqd }}</td>
          </tr>
          <tr>
            <td colspan="2">补缴税费</td>
            <td style="text-align: center">020505</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.bjsf }}</td>
          </tr>
          <tr>
            <td colspan="2">其他</td>
            <td style="text-align: center">020506</td>
            <td style="text-align: center">元</td>
            <td style="text-align: center">{{ form.qtje }}</td>
          </tr>
          <tr>
            <td colspan="2">六、审计发现问题整改（非金额类）</td>
            <td style="text-align: center">020600</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.fje }}</td>
          </tr>
          <tr>
            <td colspan="2">其中：新制定制度</td>
            <td style="text-align: center">020601</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.xzd }}</td>
          </tr>
          <tr>
            <td colspan="2">修订完善制度</td>
            <td style="text-align: center">020602</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.xdzd }}</td>
          </tr>
          <tr>
            <td colspan="2">优化完善业务流程</td>
            <td style="text-align: center">020603</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.yhlc }}</td>
          </tr>
          <tr>
            <td colspan="2">其他</td>
            <td style="text-align: center">020604</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center">{{ form.qt }}</td>
          </tr>
          <tr>
            <td colspan="2">七、根据审计建议给予党纪、政务和内部纪律处分</td>
            <td style="text-align: center">020700</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center"></td>
          </tr>
          <tr>
            <td colspan="2">其中：党纪处分</td>
            <td style="text-align: center">020701</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center"></td>
          </tr>
          <tr>
            <td colspan="2">政务处分</td>
            <td style="text-align: center">020702</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center"></td>
          </tr>
          <tr>
            <td colspan="2">内部纪律处分</td>
            <td style="text-align: center">020703</td>
            <td style="text-align: center">个</td>
            <td style="text-align: center"></td>
          </tr>
          <tr>
            <td colspan="2">八、向司法机关移送或报告案件线索</td>
            <td style="text-align: center">020800</td>
            <td style="text-align: center">件</td>
            <td style="text-align: center"></td>
          </tr>
          <tr>
            <td colspan="2">其中：涉案人员</td>
            <td style="text-align: center">020801</td>
            <td style="text-align: center">人</td>
            <td style="text-align: center"></td>
          </tr>
        </tbody>
      </table>

      <!-- <div class="footer">
        <div class="row">
          <div class="item">
            <div class="item-label">统计负责人</div>
            <div class="item-value"></div>
          </div>
          <div class="item">
            <div class="item-label">填表人</div>
            <div class="item-value"></div>
          </div>
        </div>
        <div class="row">
          <div class="item">
            <div class="item-label">联系电话</div>
            <div class="item-value"></div>
          </div>
          <div class="item">
            <div class="item-label">填表日期</div>
            <div class="item-value"></div>
          </div>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
  import { getSJYWQKBInfo, exploreSJYWQKB } from '@/oapi/audit/home'
  export default {
    data() {
      return {
        form: {},
      }
    },
    async created() {
      const res = await getSJYWQKBInfo()
      this.form = res.data.data
    },
    methods: {
      async handleExport(row) {
        const data = await exploreSJYWQKB()
        let fileName = '审计业务情况表'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
    },
  }
</script>

<style scoped>
  .table-content {
    width: 1000px;
    margin: 0 auto;
    padding: 0 20px;
  }
  .table-title {
    text-align: center;
  }
  .table-info {
    margin-bottom: 10px;
  }
  .xwtable {
    width: 100%;
    border-collapse: collapse;
  }
  .xwtable thead td {
    font-size: 12px;
    color: #000;
    text-align: center;
    border: 1px solid #ccc;
    font-weight: bold;
  }
  .xwtable tbody tr {
    background: #fff;
    font-size: 12px;
    color: #666666;
  }
  .xwtable tbody tr.alt-row {
    background: #f2f7fc;
  }
  .xwtable td {
    line-height: 20px;
    text-align: left;
    padding: 4px 10px 3px 10px;
    height: 18px;
    border: 1px solid #ccc;
  }

  .xwtable .no-border-tr td {
    border: 0;
  }

  .footer {
    width: 100%;
    margin-top: 50px;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }

  .item {
    display: flex;
    margin-right: 40px;
  }

  .row {
    display: flex;
    margin-bottom: 10px;
  }

  .item-value {
    width: 100px;
    border-bottom: 1px solid #000;
    padding-left: 10px;
  }
</style>
