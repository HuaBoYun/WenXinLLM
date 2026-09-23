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
    right: 0;
    left: 0;
    height: 2px;
    content: '';
    background-color: #1890ff;
    transition: transform 0.5s ease;
    transform: scaleX(0);
    transform-origin: bottom right;
  }

  span:hover::before {
    transform: scaleX(1);
    transform-origin: bottom left;
  }
</style>
