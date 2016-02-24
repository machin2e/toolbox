#ifndef MESSAGE_BUFFER_H
#define MESSAGE_BUFFER_H

#include <string.h>

#include "Message.h"

/**
 * Message Queue
 */

#define MAXIMUM_MESSAGE_COUNT 10

typedef struct Message_Buffer {
  Message message;
  int front;
  int back;
  int size;
} Message_Queue;

// union Message_Buffer {
//   Message message;
//   char *bytes;
// }

extern Message_Buffer incomingMessageBuffer;

extern bool Initialize_Message_Buffer (Message_Buffer *message_buffer);
extern int Buffer_Message_Byte (Message_Buffer *message_buffer, char byte);
extern int Buffer_Message_Bytes (Message_Buffer *message_buffer, char *bytes);
extern int Clear_Message_Buffer (Message_Buffer *message_buffer);
// extern Message* Get_Next_Message (Message_Queue *message_queue);
// extern int Queue_Message (Message_Queue *message_queue, Message *message);
// extern Message* Peek_Message (Message_Queue *message_queue);
// extern Message* Dequeue_Message (Message_Queue *message_queue);
extern bool Has_Partial_Buffered_Message (Message_Buffer *message_buffer);
extern bool Has_Buffered_Message (Message_Buffer *message_buffer);

#endif
