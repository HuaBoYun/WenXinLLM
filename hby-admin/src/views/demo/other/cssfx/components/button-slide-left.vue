<template>
  <button>Slide</button>
</template>

<style scoped>
  button {
    position: relative;
    z-index: 1;
    padding: 0.5em 1em;
    font-family: inherit;
    font-size: inherit;
    color: white;
    background-color: hsl(236, 32%, 26%);
    border: none;
    outline: none;
  }

  button::before {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: -1;
    content: '';
    background-color: #1890ff;
    transition: transform 0.25s ease-in-out;
    transform: scaleX(0);
    transform-origin: left center;
  }

  button:hover {
    cursor: pointer;
  }

  button:hover::before {
    transform: scaleX(1);
    transform-origin: right center;
  }
</style>
