import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import Hello from "../index.vue";
describe("Hello", () => {
  test("挂载组件", async () => {
    const wrapper = mount(Hello, {
      props: {
        count: 4,
      },
    });
    await wrapper.get("button").trigger("click");

    expect(wrapper.text()).toContain("4 x 3 = 12");

    await wrapper.get("button").trigger("click");

    expect(wrapper.text()).toContain("4 x 4 = 16");
  });
});