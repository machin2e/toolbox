#include "Message_Queue.h"

Message_Queue incomingMessageQueue;
Message_Queue outgoingMessageQueue;

bool Initialize_Message_Queue (Message_Queue *message_queue) {
  int i;

  // Set size of queue to zero
  (*message_queue).size = 0;
  (*message_queue).front = 0;
  (*message_queue).back = 0;

  // Initialize message queue
  for (i = 0; i < MAXIMUM_MESSAGE_COUNT; i++) {
    Initialize_Message(&((*message_queue).messages[i]), "", "", "");
  }

  return true;
}

Message* Get_Next_Message (Message_Queue *message_queue) {
  Message *message = NULL;
  if ((*message_queue).size < MAXIMUM_MESSAGE_COUNT) {
    message = &(incomingMessageQueue.messages[(*message_queue).back]);
  }
  return message;
}

int Queue_Message (Message_Queue *message_queue, Message *message) {
  if ((*message_queue).size < MAXIMUM_MESSAGE_COUNT) {
    (*message_queue).messages[(*message_queue).back] = *message;
    (*message_queue).back = ((*message_queue).back + 1) % MAXIMUM_MESSAGE_COUNT;
    (*message_queue).size++;
  }
  return (*message_queue).size;
}

Message* Peek_Message (Message_Queue *message_queue) {
  Message *message = NULL;
  if ((*message_queue).size > 0) {
    message = &((*message_queue).messages[(*message_queue).front]);
  }
  return message;
}

Message* Dequeue_Message (Message_Queue *message_queue) {
  Message *message = NULL;
  if ((*message_queue).size > 0) {
    message = &((*message_queue).messages[(*message_queue).front]);
    (*message_queue).front = ((*message_queue).front + 1) % MAXIMUM_MESSAGE_COUNT;
    (*message_queue).size--;
  }
  return message;
}

bool Has_Messages (Message_Queue *message_queue) {
  return ((*message_queue).size > 0);
}
