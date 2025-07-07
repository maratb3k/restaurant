export default function NotificationIcon({ className = "" }: { className?: string }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      className={`h-[24px] w-[24px] ${className}`}
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path d="M6 8C6 6.4087 6.63214 4.88258 7.75736 3.75736C8.88258 2.63214 10.4087 2 12 2C13.5913 2 15.1174 2.63214 16.2426 3.75736C17.3679 4.88258 18 6.4087 18 8C18 15 21 17 21 17H3C3 17 6 15 6 8Z" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M10.2998 21C10.4672 21.3044 10.7133 21.5583 11.0123 21.7352C11.3113 21.912 11.6524 22.0053 11.9998 22.0053C12.3472 22.0053 12.6883 21.912 12.9873 21.7352C13.2864 21.5583 13.5324 21.3044 13.6998 21" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}