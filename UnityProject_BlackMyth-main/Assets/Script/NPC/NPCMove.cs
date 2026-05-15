using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.UI;
public class NPCMove : MonoBehaviour
{
    private NavMeshAgent agent;
    public Transform[] points;
    private int destPoint = 0;
    private Animator animator;
    public GameObject UI;
    public Text UIText;
    public string UIst;
    private bool isPathfindingPaused = false;
    void Start()
    {
        agent = GetComponent<NavMeshAgent>();
        animator = GetComponent<Animator>();
        UIText.text = UIst;
        GotoNextPoint();
    }

    void GotoNextPoint()
    {
        if (points.Length == 0)
        {
            return;
        }

        destPoint = Random.Range(0, points.Length);
        agent.destination = points[destPoint].position;
    }

    void Update()
    {
        if (isPathfindingPaused == false)
        {
          
            if (agent.remainingDistance < 0.5f)
            {
                GotoNextPoint();
            }
        }
   

    }
    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            UI.gameObject.SetActive(true);
            isPathfindingPaused = true;
            agent.enabled = false;
            animator.enabled = false;
            Vector3 direction = other.transform.position - transform.position;
            direction.y = 0;
            transform.rotation = Quaternion.LookRotation(direction);
        
    }
}
    private void OnTriggerExit(Collider other)
    {
        if (other.tag == "Player")
        {
            UI.gameObject.SetActive(false);
            isPathfindingPaused = false;
            agent.enabled = true;
            animator.enabled = true;
            GotoNextPoint();
        }
    }
}
