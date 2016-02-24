#ifndef MESSAGE_H
#define MESSAGE_H

/**
 * Message
 */

#define MAXIMUM_MESSAGE_LENGTH 512
#define MAXIMUM_SOURCE_LENGTH 32
#define MAXIMUM_DESTINATION_LENGTH 32

typedef struct Message {
  char source[MAXIMUM_SOURCE_LENGTH];
  char destination[MAXIMUM_DESTINATION_LENGTH];
  char content[MAXIMUM_MESSAGE_LENGTH];
} Message;

// extern Message Create_Message (const char *content);
// extern Delete_Message (Message *message);
extern void Initialize_Message (Message *message, char *source, char *destination, char *content);

#endif
