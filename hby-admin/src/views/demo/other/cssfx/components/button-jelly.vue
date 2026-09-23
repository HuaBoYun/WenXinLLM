<template>
  <button>Jelly</button>
</template>

<style scoped>
  button {
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
    animation: jelly 0.5s;
  }

  @keyframes jelly {
    0%,
    100% {
      transform: scale(1, 1);
    }

    25% {
      transform: scale(0.9, 1.1);
    }

    50% {
      transform: scale(1.1, 0.9);
    }

    75% {
      transform: scale(0.95, 1.05);
    }
  }
</style>
