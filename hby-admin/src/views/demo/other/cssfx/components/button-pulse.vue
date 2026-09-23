<template>
  <button>Pulse</button>
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

  button:hover {
    cursor: pointer;
  }

  button::before {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: -1;
    content: '';
    border: 4px solid hsl(236, 32%, 26%);
    transform: scale(1);
    transform-origin: center;
  }

  button:hover::before {
    opacity: 0;
    transition: all 0.75s ease-in-out;
    transform: scale(1.75);
    transform-origin: center;
  }
</style>
