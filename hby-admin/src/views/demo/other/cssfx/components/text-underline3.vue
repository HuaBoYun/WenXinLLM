<template>
  <span>Underline</span>
</template>

<style scoped>
  span {
    position: relative;
  }

  span::before {
    position: absolute;
    bottom: 0;
    left: 50%;
    width: 100%;
    height: 2px;
    content: '';
    background-color: #1890ff;
    transition: transform 0.3s ease-in-out;
    transform: translate(-50%, 0) scaleX(0);
    transform-origin: center;
  }

  span:hover::before {
    transform: translate(-50%, 0) scaleX(1);
  }
</style>
