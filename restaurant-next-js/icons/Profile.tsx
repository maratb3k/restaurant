export default function ProfileIcon({ className = "" }: { className?: string }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      className={`h-[24px] w-[24px] ${className}`}
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path d="M18 20C18 18.4087 17.3679 16.8826 16.2426 15.7574C15.1174 14.6321 13.5913 14 12 14C10.4087 14 8.88258 14.6321 7.75736 15.7574C6.63214 16.8826 6 18.4087 6 20" strokeWidth="1.75" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M12 14C14.2091 14 16 12.2091 16 10C16 7.79086 14.2091 6 12 6C9.79086 6 8 7.79086 8 10C8 12.2091 9.79086 14 12 14Z" strokeWidth="1.75" stroke-linecap="round" stroke-linejoin="round" />
      <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" strokeWidth="1.75" stroke-linecap="round" stroke-linejoin="round" />
    </svg>
  );
}