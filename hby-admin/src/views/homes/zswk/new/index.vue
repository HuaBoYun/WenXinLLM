<template>
  <div class="container">
    <div class="index-search-box-wrapper">
      <!-- <a href="https://wenku.baidu.com/?fr=logo" class="icon-wk">
        <img
          src="@/assets/zhezi.png"
          alt="百度文库"
          class="logo-img"
          style="width: 55%; height: 100%"
        />
      </a> -->
      <div class="search-box-wrapper">
        <form onsubmit="return false" class="input-wrapper">
          <input
            placeholder="搜索文档"
            class="search-input"
            style="width: 590px"
          />
          <span class="search-btn">搜索文档</span>
        </form>
        <div class="hot-words-wrapper" style="width: 590px; display: none">
          <div class="hot-words-label">热门搜索</div>
          <div class="hot-words-item">协议书</div>
          <div class="hot-words-item">贷款合同</div>
          <div class="hot-words-item">日历表</div>
        </div>
      </div>
    </div>
    <div class="new-index-content-wrapper">
      <div class="new-index-left-wrapper">
        <div class="db-2023">
          <div class="db-head">
            <div class="head-tit"></div>
            <div class="head-right">
              <div class="db-space">
                <div class="space-head head-left">
                  <span slot="reference" class="doc-menu-btn" @click="open">
                    新建文档
                  </span>
                  <!-- <div class="new-folder-btn" @click="open">新建文件夹</div> -->
                </div>
                <div class="space-head head-right">
                  <div id="database2023-db-search" class="db-search">
                    <div class="db-search-input">
                      <div class="db-search-el-input el-input el-input--suffix">
                        <input
                          type="text"
                          autocomplete="off"
                          placeholder="搜索我的收藏、编辑、浏览"
                          class="el-input__inner"
                        />

                        <span class="el-input__suffix">
                          <span class="el-input__suffix-inner">
                            <i class="el-input__icon el-icon-search"></i>
                          </span>
                        </span>
                      </div>
                    </div>
                  </div>

                  <div
                    class="el-tooltip space-mode tile"
                    aria-describedby="el-tooltip-2926"
                    contenteditable="false"
                  ></div>
                  <div class="space-pagination">
                    <span class="arrow prev disabled"></span>
                    <span class="text">1 / 25</span>
                    <span class="arrow next"></span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="db-body">
            <div class="body-left">
              <div class="db-folder">
                <div class="folder-wrap">
                  <div title="最近浏览" class="folder-item default">
                    最近浏览
                  </div>
                  <div title="我的收藏" class="folder-item default">
                    我的收藏
                  </div>
                  <div title="我的下载" class="folder-item default">
                    我的下载
                  </div>
                  <div title="我的上传" class="folder-item default">
                    我的上传
                  </div>
                </div>
              </div>
            </div>
            <div class="body-right">
              <div class="doc-list mode-tile">
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/731df5d4770bf78a6529546c?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                  </div>
                  <div class="item-title">小学残疾儿童送教上门教案</div>
                  <div class="item-plus">
                    <div>22.1w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/84a473c2da38376baf1faed6?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <span class="icon-video"></span>

                    <div
                      class="file-type-icon file-type-icon-2022 ppt"
                      style="width: 18px; height: 18px"
                    ></div>

                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-4935"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="
                          transform-origin: center top;
                          z-index: 2075;
                          display: none;
                        "
                      >
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>

                        <div
                          x-arrow=""
                          class="popper__arrow"
                          style="left: 57px"
                        ></div>
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-4935"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">你来比划我来猜词语(超搞笑版)</div>
                  <div class="item-plus">
                    <div>155.4w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/b8f92d09fad6195f312ba621?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-4798"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="display: none"
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-4798"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">20以内的退位减法口算练习题</div>
                  <div class="item-plus">
                    <div>23.1w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/96dd979e680203d8ce2f2454?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-3584"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="
                          transform-origin: center top;
                          z-index: 2055;
                          display: none;
                        "
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                        <div
                          x-arrow=""
                          class="popper__arrow"
                          style="left: 57px"
                        ></div>
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-3584"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">《交通安全教育》主题班会教案</div>
                  <div class="item-plus">
                    <div>33.1w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/b653d23e0722192e4536f65b?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 excel"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-3457"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="
                          transform-origin: center top;
                          z-index: 2049;
                          display: none;
                        "
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                        <div
                          x-arrow=""
                          class="popper__arrow"
                          style="left: 57px"
                        ></div>
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-3457"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">
                    100以内加减法口算题-表格打印版(8000道)
                  </div>
                  <div class="item-plus">
                    <div>67.2w 阅读</div>
                  </div>
                </div>
                <div class="doc-item clearRight">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/39f0d6bb0029bd64783e2c3e?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-7109"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="
                          transform-origin: center top;
                          z-index: 2018;
                          display: none;
                        "
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                        <div
                          x-arrow=""
                          class="popper__arrow"
                          style="left: 57px"
                        ></div>
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-7109"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">弟子规全文带拼音(完整版)</div>
                  <div class="item-plus">
                    <div>36.4w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/54f65170a98271fe900ef910?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-1063"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="display: none"
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-1063"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">
                    20以内加减法练习题(100题)50份-20以内加法题目
                  </div>
                  <div class="item-plus">
                    <div>65w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/4f34961a844769eae009edfb?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-4179"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="
                          transform-origin: center top;
                          z-index: 2068;
                          display: none;
                        "
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                        <div
                          x-arrow=""
                          class="popper__arrow"
                          style="left: 8px"
                        ></div>
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-4179"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">幼儿园大班数学试题(适合6岁左右)</div>
                  <div class="item-plus">
                    <div>46.8w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/b0461b0bc281e53a5802ff2b?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-2590"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="
                          transform-origin: center top;
                          z-index: 2002;
                          display: none;
                        "
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                        <div
                          x-arrow=""
                          class="popper__arrow"
                          style="left: 8px"
                        ></div>
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-2590"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">幼儿园教职工安全培训记录</div>
                  <div class="item-plus">
                    <div>34.7w 阅读</div>
                  </div>
                </div>
                <div class="doc-item">
                  <div class="item-cover">
                    <div
                      class="cover-pic"
                      style="
                        background-image: url('https://wkimg.bdimg.com/img/36ebbeb9dd3383c4bb4cd2a4?new=1&amp;w=500&amp;p=1');
                      "
                    ></div>
                    <div class="cover-mask"></div>
                    <!---->

                    <div
                      class="file-type-icon file-type-icon-2022 word"
                      style="width: 18px; height: 18px"
                    ></div>
                    <!---->
                    <span contenteditable="false">
                      <div
                        role="tooltip"
                        id="el-popover-4153"
                        aria-hidden="true"
                        class="el-popover el-popper database2023 doc-list"
                        contenteditable="false"
                        style="display: none"
                      >
                        <!---->
                        <div class="pop-item">复制链接</div>
                        <div class="pop-item">去下载</div>
                        <div class="pop-item">分享</div>
                        <!---->
                      </div>
                      <span class="el-popover__reference-wrapper">
                        <span
                          class="icon-more el-popover__reference"
                          aria-describedby="el-popover-4153"
                          contenteditable="false"
                        ></span>
                      </span>
                    </span>
                  </div>
                  <div class="item-title">整式的加减练习100题(有答案)</div>
                  <div class="item-plus">
                    <div>25.7w 阅读</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="calendar">
          <div sula-card-name="DefaultEmptyComponent">
            <div class="pc-calendar-entry ndcore-index">
              <div class="entry-head">
                <div class="head-left">
                  <div class="logo"></div>
                  <div class="fest-list">
                    <div class="fest-item">大雪</div>
                    <div class="fest-item">感恩节</div>
                    <div class="fest-item active">
                      南京大屠杀死难者国家公祭日
                    </div>
                    <div class="fest-item">冬至</div>
                    <div class="fest-item">圣诞节</div>
                  </div>
                </div>
                <div class="head-right">
                  <div class="more">查看更多</div>
                </div>
              </div>
              <div class="entry-body">
                <div class="cards" style="width: 464px; height: 195px">
                  <div class="carousel">
                    <div class="carousel-wrap">
                      <div
                        class="slide-item carousel-item active animating"
                        total="3"
                        style="transform: translateX(0px)"
                      >
                        <div
                          class="cover-head"
                          style="
                            background-image: url('https://edu-wenku.bdimg.com/v1/wap/static/img/%E9%A6%96%E9%A1%B5-1677836069758.png');
                          "
                        ></div>
                        <div class="card">
                          <div class="card-item">
                            <div class="item-head">
                              <span class="title">
                                日历文档-南京大屠杀死难者国家公祭日
                              </span>
                              <span class="date">12月13日</span>
                            </div>
                            <div class="item-body">
                              <div class="desc">
                                南京纪念日，警钟声声，铭记历史，以史为鉴，奋发图强!团结一心，走向复兴，珍爱和平，不惧战争!中国版图，神圣主权，铁血儿男，舍身捍卫!寸土不让，保卫家乡!祝福祖国，更加强势!
                              </div>
                              <div class="plus">
                                <div
                                  class="filetype-icon file-type-icon-2022 pdf"
                                  style="width: 18px; height: 18px"
                                ></div>
                                <div class="title">
                                  纪念南京大屠杀励志报国的名言
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="doc-list">
                  <div class="list-wrap">
                    <div class="list-item">
                      <div class="item-img">
                        <div
                          class="cover"
                          style="
                            background-image: url('https://wkimg.bdimg.com/img/6fa72ff26137ee06eff918d9?new=1&amp;w=500&amp;p=1');
                          "
                        ></div>
                        <div
                          data-v-66ded364=""
                          class="filetype-icon file-type-icon-2022 word"
                          style="width: 18px; height: 18px"
                        ></div>
                      </div>
                      <div class="item-title">
                        (高中议论文)纪念南京大屠杀周年作文
                      </div>
                      <div class="item-plus">
                        <span class="score">
                          <span class="star"></span>
                          <span>3.88</span>
                        </span>
                      </div>
                    </div>
                    <div class="list-item">
                      <div class="item-img">
                        <div
                          class="cover"
                          style="
                            background-image: url('https://wkimg.bdimg.com/img/92213b5ca417866fb84a8e30?new=1&amp;w=500&amp;p=1');
                          "
                        ></div>
                        <div
                          data-v-66ded364=""
                          class="filetype-icon file-type-icon-2022 word"
                          style="width: 18px; height: 18px"
                        ></div>
                      </div>
                      <div class="item-title">
                        《南京大屠杀》读后感_南京大屠杀纪念日作文1000字
                      </div>
                      <div class="item-plus">
                        <span class="score">
                          <span class="star"></span>
                          <span>3.71</span>
                        </span>
                      </div>
                    </div>
                    <div class="list-item">
                      <div class="item-img">
                        <div
                          class="cover"
                          style="
                            background-image: url('https://wkimg.bdimg.com/img/85f1b4b3b9d528ea81c779f4?new=1&amp;w=500&amp;p=1');
                          "
                        ></div>
                        <div
                          data-v-66ded364=""
                          class="filetype-icon file-type-icon-2022 word"
                          style="width: 18px; height: 18px"
                        ></div>
                      </div>
                      <div class="item-title">
                        精彩作文大全-南京大屠杀演讲稿记住鲜血写成的历史 精品
                      </div>
                      <div class="item-plus">
                        <span class="score">
                          <span class="star"></span>
                          <span>4.11</span>
                        </span>
                      </div>
                    </div>
                    <div class="list-item">
                      <div class="item-img">
                        <div
                          class="cover"
                          style="
                            background-image: url('https://wkimg.bdimg.com/img/54404d3902020740be1e9b9f?new=1&amp;w=500&amp;p=1');
                          "
                        ></div>
                        <div
                          data-v-66ded364=""
                          class="filetype-icon file-type-icon-2022 word"
                          style="width: 18px; height: 18px"
                        ></div>
                      </div>
                      <div class="item-title">南京大屠杀公祭日活动方案</div>
                      <div class="item-plus">
                        <span class="score">
                          <span class="star"></span>
                          <span>4.12</span>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="operation-wrapper box-style">
          <div class="box-title-wrapper">
            <span
              class="title-text"
              style="
                width: 43px;
                background-image: url('https://edu-wenku.bdimg.com/v1/pc/2022-new-index/jingxuan-1659700032409.png');
              "
            ></span>
            <span class="tab-item active">热门推荐</span>
          </div>
          <div class="list-content">
            <div class="operation-item-content">
              <div
                class="operation-item"
                style="
                  background-image: url('https://edu-wenku.bdimg.com/v1/pc/shop/major-home-1653290617575.png');
                "
              ></div>
            </div>
            <div class="operation-item-content">
              <div
                class="operation-item"
                style="
                  background-image: url('https://edu-wenku.bdimg.com/v1/pc/shop/major-home-1653290617575.png');
                "
              ></div>
            </div>
            <div class="operation-item-content">
              <div
                class="operation-item"
                style="
                  background-image: url('https://edu-wenku.bdimg.com/v1/pc/shop/major-home-1653290617575.png');
                "
              ></div>
            </div>
            <div class="operation-item-content">
              <div
                class="operation-item"
                style="
                  background-image: url('https://edu-wenku.bdimg.com/v1/pc/shop/major-home-1653290617575.png');
                "
              ></div>
            </div>
          </div>
        </div> -->

        <div id="doc-rec-wrapper" class="doc-rec-wrapper box-style">
          <div data-v-7957254b="" class="box-title-wrapper">
            <span
              data-v-7957254b=""
              class="title-text"
              style="
                width: 43px;
                background-image: url('https://edu-wenku.bdimg.com/v1/pc/2022-new-index/wendang-1659700415688.png');
              "
            ></span>
            <span data-v-7957254b="" class="tab-item">基础教育</span>
            <span data-v-7957254b="" class="tab-item">语言/资格考试</span>
            <span data-v-7957254b="" class="tab-item">建筑</span>
            <span data-v-7957254b="" class="tab-item active">行业资料</span>
            <span data-v-7957254b="" class="tab-item">实用模板</span>
          </div>
          <div class="subscription">
            <span class="icon"></span>
            <span class="text" @click="fetchData">刷新</span>
          </div>
          <div class="doc-rec">
            <div class="list-wrap" style="">
              <div class="list-item" v-for="(item, index) in list" :key="index">
                <!-- <div class="close_button" @click="handleDelete(item.libraryid)">
                  <i class="el-icon-delete"></i>
                </div> -->
                <div
                  class="doc-item-vertical"
                  @click="handleCLick(item.doc_url, item.id)"
                >
                  <div class="doc-cover-wrapper">
                    <div class="cover-wrapper">
                      <div
                        class="cover"
                        :style="{
                          backgroundImage: 'url(' + item.thumbnail + ')',
                        }"
                      ></div>
                    </div>

                    <div data-v-50e20b64="" class="new-doc-type-icon pdf"></div>
                  </div>
                  <div class="name">{{ item.doc_name }}</div>
                  <div class="info-wrap">
                    <span class="star-ic"></span>
                    <span class="score">4.2</span>
                    <span class="view-num">
                      {{ item.ckcount || 0 }}&nbsp;阅读
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- <div class="new-index-right-wrapper">
        <div class="recommend-list right-box-style">
          <div class="title-content">热门文档分类</div>
          <div class="list-content list-hidden">
            <div>
              <div class="list-item">专利</div>
              <div class="list-item">优质办公模板</div>
              <div class="list-item">高校与高等教育</div>
              <div class="list-item">语言/资格考试</div>
              <div class="list-item">实用模板</div>
              <div class="list-item">法律</div>
              <div class="list-item">建筑</div>
              <div class="list-item">互联网</div>
              <div class="list-item">行业资料</div>
              <div class="list-item">政务民生</div>
              <div class="list-item">说明书</div>
              <div class="list-item">生活娱乐</div>
            </div>
          </div>
        </div>
        <div class="recommend-list right-box-style">
          <div class="title-content">品牌内容</div>
          <div class="list-content list-hidden" style="max-height: 140px">
            <div>
              <div class="list-item">艺术留学</div>
              <div class="list-item">装修</div>
              <div class="list-item">学历提升</div>
              <div class="list-item">考研培训</div>
              <div class="list-item">在职考研</div>
              <div class="list-item">人事管理软件</div>
              <div class="list-item">MBA</div>
              <div class="list-item">执业医师</div>
              <div class="list-item">教师资格证</div>
              <div class="list-item">初级会计</div>
              <div class="list-item">思维导图</div>
              <div class="list-item">日语培训</div>
            </div>
          </div>
        </div>
      </div> -->
    </div>
    <Edit ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import Edit from './edit.vue'
  import { wkList, deleteWKList, previewWK } from '@/api/setting/auth'
  import axios from 'axios'
  export default {
    name: 'wk',
    components: { Edit },
    data() {
      return {
        list: [],
      }
    },
    async created() {
      this.fetchData()
    },
    methods: {
      open() {
        this.$refs.edit.showEdit()
      },
      handleDelete(id) {
        deleteWKList({ libraryid: id }).then((res) => {
          if (res.code == 1) {
            this.$message.success('删除成功')
            this.fetchData()
            return
          }
          this.$message.error('删除失败')
          this.fetchData()
        })
      },
      handleCLick(url, id) {
        // previewWK({ libraryid: id }).then((res) => {
        //   if (res.code == 1) {
        //     this.fetchData()
        //   }
        // })

        window.open(url, '_blank')
      },
      async fetchData() {
        // const arr = await wkList()
        // this.list = arr.data.pageInfo.tlist
        axios({
          url: `https://office.wenxin.example.com/api/app/docshare/`,
          method: 'get',
        }).then((res) => {
          if (res.data.results) {
            this.list = res.data.results
          }
        })
      },
    },
  }
</script>

<style scoped lang="scss">
  .container {
    background-color: #f5f5f5;
  }
  .index-search-box-wrapper {
    font-size: 0;
    margin: 0 auto;
    padding-top: 16px;
    position: relative;
    width: 838px;
    .icon-wk {
      cursor: pointer;
      display: inline-block;
      height: 36px;
      margin-right: -10px;
      margin-left: 20px;
      margin-top: 2px;
      outline: none;
      position: relative;
      vertical-align: top;
      width: 64px;
    }
  }
  .search-box-wrapper {
    display: inline-block;
    position: relative;
    .search-input {
      border: 2px solid #c4c7ce;
      border-radius: 10px 0 0 10px;
      border-right: none;
      box-sizing: border-box;
      display: inline-block;
      font-size: 16px;
      height: 40px;
      outline: 0;
      padding: 13px 62px 12px 21px;
      vertical-align: top;
    }
    .search-btn {
      background: #4e6ef2;
      border-radius: 0 10px 10px 0;
      color: #fff;
      cursor: pointer;
      display: inline-block;
      font-size: 17px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      width: 112px;
    }
  }
  .new-index-content-wrapper {
    margin: 0 auto;
    position: relative;
    width: 100%;
  }

  .new-index-left-wrapper {
    margin: 39px 0 32px;
    position: relative;
    width: 100%;
    .db-2023 {
      background: #fff;
      border-radius: 8px;
      box-sizing: border-box;
      margin-top: 16px;
      padding: 28px;
      position: relative;
      width: 100%;
      .db-head {
        align-items: center;
        display: flex;
        justify-content: space-between;
        .head-tit {
          background: url(https://edu-wenku.bdimg.com/v1/pc/database/database-title.svg);
          background-position: 0 2px;
          background-repeat: no-repeat;
          height: 32px;
          margin-right: 100px;
          width: 108px;
        }
        .head-right {
          align-items: center;
          display: flex;
          flex: 1;
          justify-content: space-between;
          .db-space {
            align-items: center;
            display: flex;
            color: #666;
            flex: 1;
            font-family: PingFangSC-Regular;
            font-size: 12px;
            justify-content: space-between;
            .head-left {
              flex: 1;
              display: flex;
              .doc-menu-btn {
                align-items: center;
                background: #0075ff;
                border-radius: 6px;
                box-sizing: border-box;
                color: #fff;
                cursor: pointer;
                display: flex;
                font-size: 13px;
                height: 32px;
                justify-content: center;
                line-height: 30px;
                margin-right: 16px;
                text-align: center;
                width: 101px;
              }
              .new-folder-btn {
                border: 1px solid #ccc;
                border-radius: 6px;
                box-sizing: border-box;
                color: #333;
                cursor: pointer;
                display: inline-block;
                font-size: 13px;
                font-weight: 500;
                height: 32px;
                letter-spacing: 0;
                line-height: 30px;
                text-align: center;
                width: 109px;
              }
            }
            .head-right {
              .db-search {
                position: relative;
                width: 224px;
              }
              .space-mode {
                border-radius: 4px;
                cursor: pointer;
                height: 22px;
                margin-left: 14px;
                position: relative;
                width: 22px;
              }
              .arrow {
                background-position: 50%;
                background-repeat: no-repeat;
                background-size: 10px 10px;
                border-radius: 12px;
                box-shadow: 0 3px 7px -2px rgba(77, 77, 77, 0.19);
                display: inline-block;
                height: 20px;
                width: 20px;
                cursor: pointer;
              }
              .prev {
                background-image: url(https://edu-wenku.bdimg.com/v1/pc/database/arrow-left-ban.svg);
              }
              .next {
                background-image: url(https://edu-wenku.bdimg.com/v1/pc/database/arrow-right.svg);
              }
              .space-pagination {
                align-items: center;
                display: flex;
                justify-content: space-between;
                margin: 0 0 0 20px;
              }
            }
          }
        }
      }
      .db-body {
        align-items: flex-start;
        display: flex;
        height: 424px;
        justify-content: space-between;
        margin-top: 20px;
        .body-left {
          background: #f5f5f5;
          border-radius: 8px;
          box-sizing: border-box;
          display: flex;
          flex-direction: column;
          height: 100%;
          justify-content: space-between;
          overflow: hidden;
          width: 180px;
          .db-folder {
            align-items: center;
            display: flex;
            flex: 1;
            flex-direction: column;
            justify-content: center;
            position: relative;
            .folder-wrap {
              bottom: 70px;
              left: 0;
              overflow-y: auto;
              padding: 10px 0;
              position: absolute;
              right: 0;
              top: 0;
              width: 100%;
              .folder-item {
                color: #222;
                cursor: pointer;
                font-family: PingFangSC-Regular;
                font-size: 13px;
                overflow: hidden;
                padding: 10px 20px;
                position: relative;
                text-align: left;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
            }
          }
        }
        .body-right {
          flex: 1;
          height: 100%;
          position: relative;
          .doc-list {
            height: 100%;
            padding-left: 30px;
            text-align: left;
            .doc-item {
              cursor: pointer;
              display: inline-block;
              margin: 0 16px 20px 18px;
              width: 109px;
              .item-cover {
                position: relative;
                top: 0;
                transition-duration: 0.1s;
                transition-property: top;
                &:hover {
                  top: -4px;
                }
                .cover-pic {
                  background: #fff;
                  background-position: 50%;
                  background-repeat: no-repeat;
                  background-size: contain;
                  border: 1px solid #e0e0e0;
                  border-radius: 6px;
                  box-shadow: 4px 4px 0 0 rgba(0, 0, 0, 0.04);
                  box-sizing: border-box;
                  height: 152px;
                  overflow: hidden;
                  position: relative;
                  width: 109px;
                }
                .file-type-icon {
                  bottom: 0;
                  position: absolute;
                  right: 0;
                }
                .file-type-icon-2022.word {
                  background-image: url(https://edu-wenku.bdimg.com/v1/pc/view/FileTypeIcon2022/word.svg);
                  background-repeat: no-repeat;
                  background-size: 100% 100%;
                  float: left;
                  height: 18px;
                  width: 18px;
                }
                .file-type-icon-2022.ppt {
                  background-image: url(https://edu-wenku.bdimg.com/v1/pc/view/FileTypeIcon2022/ppt.svg);
                  background-repeat: no-repeat;
                  background-size: 100% 100%;
                  float: left;
                  height: 18px;
                  width: 18px;
                }
              }
              .item-title {
                color: #222;
                font-size: 14px;
                margin-top: 13px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }
              .item-plus {
                color: #999;
                font-size: 12px;
                margin-top: 6px;
              }
            }
          }
        }
      }
    }
    .calendar {
      margin: 0;
      padding: 0;
      .pc-calendar-entry {
        background: #fff;
        border-radius: 8px;
        box-sizing: border-box;
        .entry-head {
          display: flex;
          align-items: center;
          justify-content: center;
          .head-left {
            flex: 1;
            display: flex;
            margin-right: 20px;
            .logo {
              width: 90px;
              height: 40px;
              background-image: url(https://edu-wenku.bdimg.com/v1/pc/sulacard/PCCalendarEntry/logo-index.svg);
              background-size: cover;
              background-repeat: no-repeat;
              background-position: center 6px;
            }
            .fest-list {
              flex: 1;
              display: flex;
              align-items: center;
              justify-content: flex-start;
              margin-left: 20px;
              .fest-item {
                height: 27px;
                line-height: 27px;
                background: #f4f4f4;
                border-radius: 4px;
                font-family: PingFangSC-Regular;
                font-size: 13px;
                color: #222;
                font-weight: 400;
                padding: 0 8px;
                margin-right: 8px;
                cursor: pointer;
              }
            }
          }
          .head-right {
            .more {
              width: 87px;
              height: 27px;
              line-height: 25px;
              box-sizing: border-box;
              border: 1px solid rgba(204, 204, 204, 0.7);
              border-radius: 4px;
              font-family: PingFangSC-Regular;
              font-size: 13px;
              color: #222;
              text-align: center;
              cursor: pointer;
              display: flex;
              align-items: center;
              justify-content: center;
              padding-left: 5px;
            }
          }
        }
        .entry-body {
          margin-top: 20px;
          display: flex;
          align-items: flex-start;
          justify-content: flex-start;
          .cards {
            top: -18px;
            .carouse {
              position: absolute;
              top: 0;
              left: 0;
              overflow-x: hidden;
              overflow-y: hidden;
              width: 100%;
              height: 100%;
              .carousel-wrap {
                position: relative;
                height: 100%;
                .carousel-item {
                  position: absolute;
                  top: 0;
                  left: 0;
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  z-index: 0;
                  display: inline-block;
                }
              }
            }
          }
        }
        .doc-list {
          margin-left: 22px;
          .list-item {
            display: inline-block;
            width: 104px;
            margin: 0 10px 0 25px;
            .item-img {
              width: 104px;
              height: 145px;
              border: 1px solid rgba(0, 0, 0, 0.1);
              border-radius: 6px;
              position: relative;
              box-sizing: border-box;
              background: #fff;
              box-shadow: 4px 4px 0 0 rgba(0, 0, 0, 0.04);
              cursor: pointer;
              position: relative;
              top: 0;
              transition-duration: 0.1s;
              transition-property: top;
              overflow: hidden;
              .cover {
                width: 100%;
                height: 100%;
                background-repeat: no-repeat;
                background-size: 100%;
                background-position: center center;
                overflow: hidden;
              }
              .filetype-icon {
                position: absolute;
                right: 0;
                bottom: 0;
                margin-right: 0;
                z-index: 2;
                background-color: #fff;
              }
            }
            .item-title {
              margin-top: 10px;
              font-size: 14px;
              color: #222;
              font-family: PingFangSC-Regular;
              text-align: left;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              cursor: pointer;
            }
            .item-plus {
              display: flex;
              align-items: center;
              margin-top: 6px;
              font-family: PingFangSC-Regular;
              font-size: 13px;
              color: #999;
              overflow: hidden;
              text-align: left;
              .score {
                color: #fd503e;
                margin: 0 20px 0 2px;
                .star {
                  display: inline-block;
                  width: 14px;
                  height: 14px;
                  background-image: url(https://edu-wenku.bdimg.com/v1/pc/pcactivity/calendarDoc/icon-star.svg);
                  background-size: cover;
                  background-repeat: no-repeat;
                  background-position: center;
                  margin-bottom: -2px;
                }
              }
            }
          }
        }
      }
      .ndcore-index {
        margin-top: 16px !important;
        padding: 30px 28px;
      }
    }
    .box-style {
      background-color: #fff;
      border-radius: 8px;
      box-sizing: border-box;
      padding: 32px;
      width: 100% !important;
    }
    .operation-wrapper {
      height: 245px;
      margin-top: 16px;
      width: 1000px;
      .box-title-wrapper {
        font-size: 0;
        .title-text {
          background-repeat: no-repeat;
          background-size: 100% 100%;
          color: #222;
          display: inline-block;
          font-size: 26px;
          font-weight: 700;
          height: 31px;
          margin-right: 24px;
          position: relative;
        }
        .tab-item {
          background: #f4f4f4;
          border-radius: 5px;
          color: #222;
          cursor: pointer;
          display: inline-block;
          font-size: 13px;
          height: 27px;
          line-height: 27px;
          margin-right: 8px;
          padding: 0 8px;
          vertical-align: top;
        }
        .active {
          background: rgba(34, 171, 130, 0.08);
          color: #22ab82;
        }
      }
      .list-content {
        margin-top: 18px;
        display: flex;
        justify-content: space-around;
        .operation-item-content {
          border-radius: 8px;
          display: inline-block;
          height: 130px;
          margin-right: 13px;
          overflow: hidden;
          width: 220px;
          .operation-item {
            background-position: 50% 50%;
            background-repeat: no-repeat;
            background-size: 224px 130px;
            border-radius: 6px;
            cursor: pointer;
            height: 130px;
            -webkit-transform-origin: center;
            transform-origin: center;
            -webkit-transition: -webkit-transform 0.4s ease 0s;
            transition: transform 0.4s ease 0s;
            width: 224px;
          }
        }
      }
    }
    .doc-rec-wrapper {
      margin-top: 16px;
      padding-right: 16px !important;
      width: 1000px;
    }
    .box-style {
      background-color: #fff;
      border-radius: 8px;
      box-sizing: border-box;
      padding: 32px;
      .box-title-wrapper {
        font-size: 0;
        .title-text {
          background-repeat: no-repeat;
          background-size: 100% 100%;
          color: #222;
          display: inline-block;
          font-size: 26px;
          font-weight: 700;
          height: 31px;
          margin-right: 24px;
          position: relative;
        }
        .tab-item {
          background: #f4f4f4;
          border-radius: 5px;
          color: #222;
          cursor: pointer;
          display: inline-block;
          font-size: 13px;
          height: 27px;
          line-height: 27px;
          margin-right: 8px;
          padding: 0 8px;
          vertical-align: top;
        }
      }
      .subscription {
        border: 1px solid hsla(0, 0%, 80%, 0.7);
        border-radius: 4px;
        cursor: pointer;
        height: 24px;
        margin-top: -31px;
        padding-left: 7px;
        padding-top: 1px;
        position: absolute;
        right: 30px;
        width: 78px;
      }
      .doc-rec {
        .list-wrap {
          vertical-align: top;
          display: flex;
          flex-wrap: wrap;
          .list-item {
            position: relative;
            cursor: pointer;
            display: inline-block;
            margin: 24px 10px 0 0;
            vertical-align: top;
            &:hover .close_button {
              display: block;
            }
            .close_button {
              position: absolute;
              top: 0;
              right: 0;
              z-index: 100;
              display: none;
            }
            .doc-item-vertical {
              display: inline-block;
              width: 144px;
              .doc-cover-wrapper {
                position: relative;
                top: 0;
                transition-duration: 0.1s;
                transition-property: top;
                .cover-wrapper {
                  border: 1px solid #e0e0e0;
                  border-radius: 6px;
                  box-shadow: 4px 4px 0 0 rgba(0, 0, 0, 0.04);
                  box-sizing: border-box;
                  display: inline-block;
                  height: 200px;
                  overflow: hidden;
                  position: relative;
                  vertical-align: top;
                  width: 144px;
                  .cover {
                    background-position: 50%;
                    background-repeat: no-repeat;
                    background-size: contain;
                    height: 200px;
                    width: 144px;
                  }
                }
                .new-doc-type-icon {
                  background-color: hsla(0, 0%, 100%, 0.6);
                  background-repeat: no-repeat;
                  background-size: 100% 100%;
                  border-radius: 2px;
                  bottom: 0;
                  height: 18px;
                  position: absolute;
                  right: 0;
                  width: 18px;
                }
              }

              .name {
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;
                color: #222;
                display: -webkit-box;
                font-size: 14px;
                margin-top: 12px;
                overflow: hidden;
                text-overflow: ellipsis;
                word-break: break-all;
              }
              .info-wrap {
                color: #fd503e;
                font-size: 0;
                margin-top: 4px;
                vertical-align: middle;
                .star-ic {
                  background-image: url(https://edu-wenku.bdimg.com/v1/ad/icon-star-1691723550424.svg);
                  background-repeat: no-repeat;
                  height: 15px;
                  margin-right: 2px;
                  width: 15px;
                }
                span {
                  display: inline-block;
                  font-size: 12px;
                  vertical-align: top;
                }
                .view-num {
                  color: #999;
                  font-size: 12px;
                  margin-left: 7.246px;
                }
              }
            }
          }
        }
      }
    }
  }
  .new-index-right-wrapper {
    position: absolute;
    right: 0;
    top: 0;
    width: 264px;
    .right-box-style {
      background-color: #fff;
      border-radius: 8px;
      box-sizing: border-box;
      margin: 0 0 16px;
      padding: 20px 20px 18px;
      .title-content {
        font-size: 16px;
        font-weight: 700;
        margin-bottom: 14px;
      }
      .list-hidden {
        max-height: 180px;
        overflow: hidden;
        .list-item {
          background: #f4f4f4;
          border-radius: 4px;
          cursor: pointer;
          display: inline-block;
          height: 28px;
          line-height: 28px;
          margin-bottom: 8px;
          margin-right: 8px;
          padding: 0 8px;
        }
      }
    }
    .recommend-list {
      color: #222;
      font-size: 13px;
      margin-top: 8px;
    }
  }
  .pc-calendar-entry.ndcore-index .entry-body .cards .cover-head {
    height: 60px;
    top: 0;
  }
  .pc-calendar-entry .entry-body .cards .cover-head {
    position: relative;
    top: -4px;
    right: 0;
    left: 0;
    background-size: 100% 100%;
    background-position: top;
    background-repeat: no-repeat;
    border-radius: 8px 8px 0 0;
  }
  .card {
    position: absolute;
    top: 20px;
    right: 0;
    bottom: 0;
    left: 0;
    .card-item {
      width: 100%;
      border-radius: 8px;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      .item-head {
        height: 42px;
        line-height: 42px;
        font-family: PingFangSC-Semibold;
        font-size: 18px;
        font-weight: 600;
        padding: 0 24px;
        display: flex;
        align-items: center;
        position: relative;
        color: #fff;
        .date {
          height: 20px;
          line-height: 18px;
          opacity: 0.7;
          border: 1px solid #fff;
          border-radius: 4px;
          font-family: PingFangSC-Regular;
          font-size: 13px;
          color: #fff;
          font-weight: 400;
          padding: 0 5px;
          margin-left: 10px;
          box-sizing: border-box;
        }
      }
      .item-body {
        position: relative;
        padding: 20px 50px 30px;
        font-family: PingFangSC-Regular;
        font-size: 14px;
        color: #111;
        font-weight: 400;
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        background: #f9f9f9;
        .desc {
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 3;
          word-break: break-all;
          cursor: pointer;
          line-height: 22px;
        }
      }
    }
  }
</style>
