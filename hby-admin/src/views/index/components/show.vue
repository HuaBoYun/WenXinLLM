<template>
  <div>
    <div class="inputdiv">
      <input
        id="companyname"
        class="seatchinput"
        name="companyname"
        placeholder="搜索公司名称"
      />
      <input
        id="reportName"
        class="seatchinput"
        name="reportName"
        placeholder=""
        type="hidden"
        value="公司列表"
      />
      <button class="seatchbtn" onClick="serchall()" type="button">
        望远镜
      </button>
      <div style="clear: both"></div>
    </div>
    <div>
      <img class="bigimg" src="../../../assets/bigimg.jpg" />
    </div>
    <!--    <iframe-->
    <!--      id="bb"-->
    <!--      class="iframe"-->
    <!--      frameborder="no"-->
    <!--      name="bb"-->
    <!--      src="/qygk/main?url=http://192.0.2.12:9001/browse/2/7C789EF9AC8D473FB5E9C9A7FA8C9463&amp;token=4A755AC65D2EDBF4A57ECEFD5E6B4678"-->
    <!--    />-->
  </div>
</template>

<script>
  export default {
    name: 'Show',
  }
</script>
<style>
  .inputdiv {
    height: 85px;
    padding-top: 20px;
  }
  .seatchinput {
    float: left;
    width: 30%;
    height: 40px;
    margin-left: 32%;
    border: 1px solid #f9373f;
    border-radius: 5px 0px 0px 5px;
  }
  input {
    outline: none;
  }

  .seatchbtn {
    float: left;
    height: 40px;
    padding: 0 20px;
    color: #fff;
    background: #f9373f;
    border: 1px solid #f9373f;
    border-radius: 0 5px 5px 0;
  }
  .bigimg {
    width: 100%;
  }
  .iframe {
    width: 100%;
    height: 1100px;
    max-height: 1100px;
    overflow: scroll;
    overflow-y: hidden;
    border: 0;
  }
</style>
