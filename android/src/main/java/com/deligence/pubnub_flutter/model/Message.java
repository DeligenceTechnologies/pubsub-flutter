package com.deligence.pubnub_flutter.model;


public class Message
{
    private String channel = "";
    private String sender = "";
    private String message = "";
    private String timestamp = "";
    private String publisher = "";

    public Message(String channel, String sender, String message, String timestamp, String publisher)
    {
        this.channel = channel;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
        this.publisher = publisher;
    }

    public Message(){}

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final Message other = (Message)obj;

        return this.channel.equals(other.channel)
                && this.sender.equals(other.sender)
                && this.message.equals(other.message)
                && this.timestamp.equals(other.timestamp);
    }

    @Override
    public int hashCode()
    {
        return 1;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    @Override public String toString()
    {
        return "Message{" +
                "channel='" + channel + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}