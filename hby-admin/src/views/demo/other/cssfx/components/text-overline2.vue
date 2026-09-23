<template>
  <span>Overline</span>
</template>

<style scoped>
  span {
    position: relative;
  }

  span::before {
    position: absolute;
    top: -2px;
    left: 50%;
    width: 100%;
    height: 2px;
    content: '';
    background-color: #1890ff;
    opacity: 0;
    transition: all 0.3s ease-in-out;
    transform: translate(-50%, 0) scaleX(0);
    transform-origin: center;
  }

  span:hover::before {
    opacity: 1;
    transform: translate(-50%, 0) scaleX(1);
  }
</style>
