<template>
  <span>Bars</span>
</template>

<style scoped>
  span {
    position: relative;
  }

  span::before,
  span::after {
    position: absolute;
    right: 0;
    left: 0;
    height: 2px;
    content: '';
    background-color: #1890ff;
    transition: transform 0.5s ease;
    transform: scaleX(0);
  }

  span::before {
    top: 0;
    transform-origin: right center;
  }

  span:hover::before {
    transform: scaleX(1);
    transform-origin: left center;
  }

  span::after {
    bottom: 0;
    transform-origin: left center;
  }

  span:hover::after {
    transform: scaleX(1);
    transform-origin: right center;
  }
</style>
