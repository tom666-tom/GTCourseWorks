using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NPCBasicMovement : MonoBehaviour
{
    public float speed = 2f;
    public float rotationSpeed = 30f;

    void Update()
    {
        // Simple forward movement
        transform.Translate(Vector3.forward * speed * Time.deltaTime);

        // Optional: Make the NPC rotate
        transform.Rotate(Vector3.up, rotationSpeed * Time.deltaTime);
    }
}

