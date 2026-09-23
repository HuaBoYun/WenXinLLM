<template>
  <button>Shine</button>
</template>

<style scoped>
  button {
    position: relative;
    z-index: 1;
    padding: 0.5em 1em;
    overflow: hidden;
    font-family: inherit;
    font-size: inherit;
    color: white;
    cursor: pointer;
    background-color: hsl(236, 32%, 26%);
    border: none;
    outline: none;
  }

  button::after {
    position: absolute;
    top: -50%;
    bottom: -50%;
    z-index: -1;
    width: 1.25em;
    content: '';
    background-color: hsla(0, 0%, 100%, 0.2);
    transform: translate3d(-525%, 0, 0) rotate(35deg);
  }

  button:hover::after {
    transition: transform 0.45s ease-in-out;
    transform: translate3d(200%, 0, 0) rotate(35deg);
  }
</style>
