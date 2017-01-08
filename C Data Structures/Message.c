#include "Message.h"

void Initialize_Message (Message *message, char *source, char *destination, char *content) {

  // Set source
  memcpy((*message).source, source, strlen(source) + 1);
  (*message).source[(strlen(source) + 1)] = '\n';
  (*message).source[(strlen(source) + 2)] = '\0';

  // Set destination
  memcpy((*message).destination, destination, strlen(destination) + 1);
  (*message).source[(strlen(destination) + 1)] = '\n';
  (*message).source[(strlen(destination) + 2)] = '\0';

  // Set contents
  memcpy((*message).content, content, strlen(content) + 1);
  (*message).source[(strlen(content) + 1)] = '\n';
  (*message).source[(strlen(content) + 2)] = '\0';
}
