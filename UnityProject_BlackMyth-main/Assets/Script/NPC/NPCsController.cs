using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

[RequireComponent(typeof(NavMeshAgent))]
public class NPCsController : MonoBehaviour
{
    private NavMeshAgent agent;
    private Animator anim;

    [Header("Basic Settings")]
    public float sightRadius;
    private float speed;

    [Header("Patrol State")]
    public float patrolRange;
    private Vector3 wayPoint;
    private Vector3 lastPosition;

    private TrashDrop trashDrop;

    //bool for animation
    bool isMove;
    bool isThrowingTrash;
    int throwCount;
    int maxThrowCount = 10;

    private float throwInterval = 2f;
    private float throwTimer = 0f;

    void Awake()
    {
        agent = GetComponent<NavMeshAgent>();
        anim = GetComponent<Animator>();
        speed = agent.speed;
        lastPosition = transform.position;
        trashDrop = GetComponent<TrashDrop>();
    }

    void Start()
    {
        GetNewWayPoint();
    }

    void Update()
    {
        SwitchAnimation();

        // Check if the NPC has reached its destination
        if (agent.remainingDistance <= agent.stoppingDistance)
        {
            GetNewWayPoint();
        }
        else if (!isThrowingTrash && throwCount < maxThrowCount)
        {
            throwTimer += Time.deltaTime;
            if (throwTimer >= throwInterval)
            {
                StartCoroutine(ThrowTrash());
                throwTimer = 0f;
            }
        }
    }

    void SwitchAnimation()
    {
        anim.SetBool("Move", isMove);
        //anim.SetBool("ThrowTrash", isThrowingTrash);
    }

    void GetNewWayPoint()
    {
        float randomX = Random.Range(-patrolRange, patrolRange);
        float randomZ = Random.Range(-patrolRange, patrolRange);

        Vector3 randomPoint = new Vector3(lastPosition.x + randomX, transform.position.y, lastPosition.z + randomZ);

        NavMeshHit hit;
        wayPoint = NavMesh.SamplePosition(randomPoint, out hit, patrolRange, 1) ? hit.position : transform.position;

        // Set the destination for the agent to start walking towards the new waypoint
        agent.SetDestination(wayPoint);
        isMove = true;
    }

    IEnumerator ThrowTrash()
    {
        if (throwCount < maxThrowCount)
        {
            isThrowingTrash = true; 
            trashDrop.Drop();

            Debug.Log("Throwing trash animation called"); 
            throwCount++;
            if (throwCount >= maxThrowCount)
            {
                throwCount = 0;
            }

            yield return new WaitForSeconds(Random.Range(10f, 20f));

            isThrowingTrash = false; 
        }
    }

    void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.blue;
        Gizmos.DrawWireSphere(transform.position, sightRadius);
    }
}