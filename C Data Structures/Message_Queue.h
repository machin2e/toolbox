#ifndef MESSAGE_QUEUE_H
#define MESSAGE_QUEUE_H

#include <string.h>

#include "Message.h"

#ifndef bool
typedef int bool;
enum { false, true };
#endif

#ifndef NULL
#define NULL 0
#endif

/**
 * Message Queue
 */

#define MAXIMUM_MESSAGE_COUNT 10

typedef struct Message_Queue {
  Message messages[MAXIMUM_MESSAGE_COUNT];
  int front;
  int back;
  int size;
} Message_Queue;

extern Message_Queue incomingMessageQueue;
extern Message_Queue outgoingMessageQueue;

extern bool Initialize_Message_Queue (Message_Queue *message_queue);
extern Message* Get_Next_Message (Message_Queue *message_queue);
extern int Queue_Message (Message_Queue *message_queue, Message *message);
extern Message* Peek_Message (Message_Queue *message_queue);
extern Message* Dequeue_Message (Message_Queue *message_queue);
extern bool Has_Messages (Message_Queue *message_queue);

#endif
