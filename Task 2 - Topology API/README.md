# Topoolgy API
A simple topology API made following the specifications in Task 2's PDF. (Not sure if I can share that here)

# Notes
* Deserialization differences
    * To make the deserialization order the same as `topology.json` I'd have to either write a custom serializer, which while not hard since it's similar to the deserializer, is extra (unneeded?) work.
    * Note that this will not make reading from the file any different, the fields are just in a different order.
    * Or we can move `netlist` to classes that inherit from `Component`, but since all components in the given example had `netlist` as a member, I chose not to.
      In the case that there are some components that don't need `netlist`, we can move that only to classes that use it.
      
* GSON Woes:
    *   Since JSON does not have any way of distinguishing number types, it will automatically convert any number it meets to `Double`. While this works, it might be suboptimal in some situations where `Floats` are faster (Since a CPU can load more floats in wide registers and thus perform SIMD operations faster). However, this does not pose a big problem in the current context of this API's use. 
      
# Assumptions
* I assumed that a device and a component are the same thing, and that the names can be used interchangeably.
* I assumed that `netlist` is a HashMap with the key as the device terminal and the value as the node. So when we search for a specific node, we first build the reverse HashMap with the node as the key and the device/component id as the value.
* I assumed that this reverse list would be constant throughout operation, hence it does nothing and returns `false` if the list is already built. If we need to rebuild this list again in case of an update, the easiest way would be to throw away the old one and rebuild it from scratch. A fast way to update would be to make the `ArrayList` a `HashSet` so that we can check cheaply if the device is connected to the node and update accordingly.      
