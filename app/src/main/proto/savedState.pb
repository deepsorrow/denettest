syntax = "proto3";

option java_package = "ru.kropotov.denet.test.proto";
option java_multiple_files = true;

message SavedState {
   string lastNodeAddress = 1;
   bool isOnEditorScreen = 2;
}